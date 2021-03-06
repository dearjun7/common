package com.bh.subject.std.common.service.reverseproxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bh.subject.std.common.error.GMSException;

public enum MethodParamConverter {
    GET {

        @Override
        public Object getMethodParameters(HttpServletRequest request, String encodingCharSet, String tmpFilePath) throws Exception {
            return this.getMethodParameters(request, encodingCharSet);
        }
    },
    POST {

        @Override
        public Object getMethodParameters(HttpServletRequest request, String encodingCharSet, String tmpFilePath) throws Exception {
            return this.getRequestBody(request);
        }
    },
    MULTI_PART {

        @Override
        public Object getMethodParameters(HttpServletRequest request, String encodingCharSet, String tmpFilePath) throws Exception {
            Map<String, Object> result = new HashMap<String, Object>();
            MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) request;
            Collection<Part> partList = multiPartRequest.getParts();
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            String[] contentTypeArr = request.getContentType().split(";");
            String boundary = contentTypeArr[1].trim().replaceAll("boundary=", "");
            List<File> tmpFileList = new ArrayList<File>();

            for(Part part : partList) {
                ContentBody contentBody = null;
                String tmpContentType = part.getContentType();

                LOGGER.debug("part Names : " + part.getName());

                if(tmpContentType != null) {
                    Map<String, Object> tmpResult = this.getFileBody(part, tmpFilePath);
                    contentBody = (ContentBody) tmpResult.get(FILE_BODY_PARAM_NAME);

                    tmpFileList.add((File) tmpResult.get(TMP_FILE_PARAM_NAME));
                } else {
                    contentBody = this.getStringBody(part, encodingCharSet);
                }

                entityBuilder.setBoundary(boundary);
                entityBuilder.addPart(part.getName(), contentBody);

                LOGGER.debug("boundary : " + boundary);
            }

            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.setCharset(Charset.forName(encodingCharSet));

            result.put(TMP_FILE_PARAM_NAME, tmpFileList);
            result.put(ENTITY_BUILDER_PARAM_NAME, entityBuilder);

            return result;
        }
    };

    public abstract Object getMethodParameters(HttpServletRequest request, String encodingCharSet, String tmpFilePath) throws Exception;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodParamConverter.class);

    private static final String FILE_BODY_PARAM_NAME = "fileBody";
    public static final String ENTITY_BUILDER_PARAM_NAME = "entityBuilder";
    public static final String TMP_FILE_PARAM_NAME = "tmpFile";

    protected Map<String, Object> getFileBody(Part part, String tmpFilePath) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        FileBody fileBody = null;
        String tmpContentType = part.getContentType();
        String tmpFileName = UUID.randomUUID().toString().replaceAll("-", "");
        File saveDir = new File(tmpFilePath);
        File file = new File(tmpFilePath + "/" + tmpFileName + "." + tmpContentType.split("/")[1]);

        if(!saveDir.exists()) {
            saveDir.mkdirs();
        }

        InputStream inputStream = null;
        OutputStream outStream = null;

        LOGGER.debug("tmp.getSubmittedFileName() : " + part.getSubmittedFileName());
        LOGGER.debug("tmpContentType : " + tmpContentType);

        try {
            inputStream = new BufferedInputStream(part.getInputStream());
            outStream = new BufferedOutputStream(new FileOutputStream(file));

            byte[] buf = new byte[1024];
            int len = 0;

            while((len = inputStream.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }

            fileBody = new FileBody(file, ContentType.create(tmpContentType), part.getSubmittedFileName());

            LOGGER.debug("contentBody.getContentLength() : " + fileBody.getContentLength());

            result.put(FILE_BODY_PARAM_NAME, fileBody);
            result.put(TMP_FILE_PARAM_NAME, file);

        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            if(outStream != null) {
                outStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return result;
    }

    protected StringBody getStringBody(Part part, String encodingCharSet) throws Exception {
        StringBody stringBody = null;
        InputStream inputStream = null;

        try {
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;

            inputStream = part.getInputStream();
            StringBuffer strBuff = new StringBuffer();

            while((bytesRead = inputStream.read(buffer)) != -1) {
                strBuff.append(new String(buffer, 0, bytesRead));
            }

            LOGGER.debug("request param value : " + strBuff);

            Charset charset = Charset.forName(encodingCharSet);
            stringBody = new StringBody(strBuff.toString(), ContentType.create("text/plain", charset));

        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return stringBody;
    }

    protected HttpEntity getRequestBody(HttpServletRequest request) throws IOException {
        InputStreamEntity result = null;
        InputStream reqInputStream = null;

        try {
            reqInputStream = new BufferedInputStream(request.getInputStream());
            result = new InputStreamEntity(reqInputStream, request.getContentLength());
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return result;
    }

    protected String getMethodParameters(HttpServletRequest request, String encodingCharSet) throws Exception {
        StringBuffer result = null;
        Enumeration<String> paramsName = request.getParameterNames();
        List<NameValuePair> getParamList = new ArrayList<NameValuePair>();

        while(paramsName.hasMoreElements()) {
            String paramName = paramsName.nextElement();
            String paramValue = URLEncoder.encode(request.getParameter(paramName), encodingCharSet);

            getParamList.add(new BasicNameValuePair(paramName, paramValue));

            LOGGER.debug("Get_Request (" + encodingCharSet + ") : " + paramName + " = " + paramValue);
        }

        result = new StringBuffer(getParamList.size());

        for(NameValuePair tmpParam : getParamList) {
            result.append("&" + tmpParam.getName() + "=" + tmpParam.getValue());
        }

        return result.toString();
    }
}
