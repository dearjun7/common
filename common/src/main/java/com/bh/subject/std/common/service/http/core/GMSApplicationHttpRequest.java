package com.bh.subject.std.common.service.http.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class GMSApplicationHttpRequest extends HttpServletRequestWrapper {

    private boolean parametersParsed = false;
    private Charset encoding = null;
    private byte[] rawData = null;
    private Map<String, ArrayList<String>> parameters = null;

    private ByteChunk tmpName = new ByteChunk();
    private ByteChunk tmpValue = new ByteChunk();

    private class ByteChunk {

        private byte[] buff;
        private int start = 0;
        private int end;

        protected void setByteChunk(byte[] b, int off, int len) {
            buff = b;
            start = off;
            end = start + len;
        }

        protected byte[] getBytes() {
            return buff;
        }

        protected int getStart() {
            return start;
        }

        protected int getEnd() {
            return end;
        }

        protected void recycle() {
            buff = null;
            start = 0;
            end = 0;
        }
    }

    public GMSApplicationHttpRequest(HttpServletRequest request) throws IOException {
        super(request);

        String characterEncoding = request.getCharacterEncoding();

        if(StringUtils.isBlank(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);

        InputStream inputStream = super.getInputStream();
        this.rawData = IOUtils.toByteArray(inputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };

        return servletInputStream;

    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    @Override
    public String getParameter(String name) {
        this.parseParameters();

        ArrayList<String> values = this.parameters.get(name);

        if(values == null || values.size() == 0) {
            return null;
        }

        return values.get(0);
    }

    public HashMap<String, String[]> getParameters() {
        this.parseParameters();

        HashMap<String, String[]> map = new HashMap<String, String[]>(this.parameters.size() * 2);

        for(String name : this.parameters.keySet()) {
            ArrayList<String> values = this.parameters.get(name);

            map.put(name, values.toArray(new String[values.size()]));
        }

        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Map getParameterMap() {
        return getParameters();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Enumeration getParameterNames() {
        return new Enumeration<String>() {

            private String[] arr = (String[]) (getParameterMap().keySet().toArray(new String[0]));
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < arr.length;
            }

            @Override
            public String nextElement() {
                return arr[index++];
            }
        };
    }

    @Override
    public String[] getParameterValues(String name) {
        this.parseParameters();

        ArrayList<String> values = this.parameters.get(name);

        if(values == null) {
            return null;
        }

        String[] arr = values.toArray(new String[values.size()]);

        if(arr == null) {
            return null;
        }

        return arr;
    }

    private void parseParameters() {
        if(this.parametersParsed) {
            return;
        }

        this.parameters = this.extractRequestParamsMap(super.getRequest().getParameterNames());
        this.parametersParsed = true;
    }

    private Map<String, ArrayList<String>> extractRequestParamsMap(Enumeration<String> parameterNames) {
        Map<String, ArrayList<String>> parameters = new HashMap<String, ArrayList<String>>();

        if(parameterNames.hasMoreElements()) {
            while(parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String[] parameterValues = super.getRequest().getParameterValues(parameterName);

                parameters.put(parameterName, new ArrayList<String>(Arrays.asList(parameterValues)));
            }
        }

        if(parameters.size() <= 0 && StringUtils.isNotBlank(super.getContentType())
                && (super.getContentType().trim().toLowerCase().startsWith("application/x-www-form-urlencoded"))) {
            this.setDecodedRequestParams(parameters);
        }

        return parameters;
    }

    private void setDecodedRequestParams(Map<String, ArrayList<String>> dest) {
        int pos = 0;
        int end = this.rawData.length;

        while(pos < end) {
            int nameStart = pos;
            int nameEnd = -1;
            int valueStart = -1;
            int valueEnd = -1;

            boolean parsingName = true;
            boolean decodeName = false;
            boolean decodeValue = false;
            boolean parameterComplete = false;

            do {
                switch(this.rawData[pos]) {
                    case '=':
                        if(parsingName) {
                            nameEnd = pos;
                            parsingName = false;
                            valueStart = ++pos;
                        } else {
                            pos++;
                        }
                        break;
                    case '&':
                        if(parsingName) {
                            nameEnd = pos;
                        } else {
                            valueEnd = pos;
                        }
                        parameterComplete = true;
                        pos++;
                        break;
                    case '%':
                    case '+':
                        if(parsingName) {
                            decodeName = true;
                        } else {
                            decodeValue = true;
                        }
                        pos++;
                        break;
                    default:
                        pos++;
                        break;
                }
            } while(!parameterComplete && pos < end);

            if(pos == end) {
                if(nameEnd == -1) {
                    nameEnd = pos;
                } else if(valueStart > -1 && valueEnd == -1) {
                    valueEnd = pos;
                }
            }

            if(nameEnd <= nameStart) {
                continue;
            }

            tmpName.setByteChunk(this.rawData, nameStart, nameEnd - nameStart);

            if(valueStart >= 0) {
                tmpValue.setByteChunk(this.rawData, valueStart, valueEnd - valueStart);
            } else {
                tmpValue.setByteChunk(this.rawData, 0, 0);
            }

            try {
                String name;
                String value;

                if(decodeName) {
                    name = new String(URLCodec.decodeUrl(Arrays.copyOfRange(tmpName.getBytes(), tmpName.getStart(), tmpName.getEnd())),
                            this.encoding);
                } else {
                    name = new String(tmpName.getBytes(), tmpName.getStart(), tmpName.getEnd() - tmpName.getStart(), this.encoding);
                }

                if(valueStart >= 0) {
                    if(decodeValue) {
                        value = new String(
                                URLCodec.decodeUrl(Arrays.copyOfRange(tmpValue.getBytes(), tmpValue.getStart(), tmpValue.getEnd())),
                                this.encoding);
                    } else {
                        value = new String(tmpValue.getBytes(), tmpValue.getStart(), tmpValue.getEnd() - tmpValue.getStart(),
                                this.encoding);
                    }
                } else {
                    value = "";
                }

                if(StringUtils.isNotBlank(name)) {
                    ArrayList<String> values = dest.get(name);

                    if(values == null) {
                        values = new ArrayList<String>(1);
                        dest.put(name, values);
                    }

                    if(StringUtils.isNotBlank(value)) {
                        values.add(value);
                    }
                }
            } catch(DecoderException e) {}

            tmpName.recycle();
            tmpValue.recycle();
        }
    }
}
