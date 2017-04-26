package com.bh.subject.std.common.service.http.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS(Cross Site Script) 공격을 방지하기 위한
 * HttpServletRequest 의 wrapper 클래스.
 * 
 * @author BH Jun
 * @version
 */
public final class XSSHttpRequest extends HttpServletRequestWrapper {

    /**
     * 
     */
    private HttpServletRequest request;
    /**
     * 
     */
    protected String filterLevel;

    /**
     * 생성자.
     * 
     * @param request
     *            HttpServletRequest
     */
    public XSSHttpRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    /**
     * @param filterLevel
     */
    public void setFilterLevel(String filterLevel) {
        this.filterLevel = filterLevel.toUpperCase();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#getRequest()
     */
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 파라메터에 해당하는 값을 XSS가 제거된 값으로 변환되어 배열로 반환한다.
     * 
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);

        if(values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];

        for(int i = 0; i < count; i++) {
            encodedValues[i] = convert(values[i]);
        }

        return encodedValues;
    }

    /**
     * 파라메터에 해당하는 값을 XSS가 제거된 값으로 변환하여 반환한다.
     * 
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);

        if(value == null) {
            return null;
        }

        return convert(value);
    }

    /**
     * 파라메터에 해당하는 값을 XSS가 제거된 값으로 변환하여 반환한다.
     * 
     * @see javax.servlet.ServletRequestWrapper#getParameterMap()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map getParameterMap() {
        Map parameterMap = super.getParameterMap();
        Map<Object, Object> newParameterMap = new HashMap<Object, Object>(parameterMap);

        Set<Object> keys = parameterMap.keySet();

        for(Object key : keys) {
            if(parameterMap.get(key) instanceof String[]) {
                String[] values = (String[]) parameterMap.get(key);
                if(values == null) {
                    newParameterMap.put(key, values);
                    continue;
                }

                for(int idx = 0; idx < values.length; idx++) {
                    values[idx] = convert(values[idx]);
                }
                newParameterMap.put(key, values);

            } else if(parameterMap.get(key) instanceof String) {
                String value = (String) parameterMap.get(key);
                if(value == null) {
                    continue;
                }

                newParameterMap.put(key, convert(value));
            }
        }

        return newParameterMap;
    }

    /**
     * 헤더에 해당하는 헤더값을 XSS가 제거된 값으로 변환하여 반환한다.
     * 
     * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);

        if(value == null) {
            return null;
        }

        return convert(value);
    }

    /**
     * 입력된 값을 XSS가 제거된 값으로 변환한다.
     * <p>
     * 변환 처리는 정규식을 이용하여 처리하며, 기본적으로 아래의 값을 걸러낸다.
     * <ul>
     * <li>&lt;html:script&gt;</li>
     * <li>&lt;/html:script&gt;</li>
     * <li>&lt;script&gt;</li>
     * <li>&lt;/script&gt;</li>
     * <li>&lt;iframe&gt;</li>
     * <li>&lt;/iframe&gt;</li>
     * <li>javascript:</li>
     * <li>vbscript:</li>
     * </ul>
     * 다음의 값들은 html 특수문자로 치환한다.
     * <ul>
     * <li>&lt; → &amp;lt;</li>
     * <li>&gt; → &amp;gt;</li>
     * </ul>
     * <p>
     * 정규식 처리에 대한 테스트는 다음의 url 을 통해 확인히 가능하다.
     * <code>http://www.regexplanet.com/advanced/java/index.html</code>
     * 
     * @param value
     *            변환할 값.
     * @return 변환된 값.
     */
    private String convert(String value) {
        if(value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and
            // uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            if("LOW".equals(this.filterLevel) || "HIGH".equals(this.filterLevel)) {
                // Avoid null characters
                value = value.replaceAll("", "");

                // Avoid anything between script tags
                Pattern scriptPattern = Pattern.compile("<script(.*?)>(.*?)</script(.*?)>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid anything in a src='...' type of expression
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                // Remove any lonesome </script> tag
                scriptPattern = Pattern.compile("</?script(.*?)>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");

                // Remove any lonesome <script ...> tag
                scriptPattern = Pattern.compile("</?html:(\\s)*script(.*?)>",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid eval(...) expressions
                scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid expression(...) expressions
                scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid javascript:... expressions
                scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid vbscript:... expressions
                scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");

                // Avoid 'onload=' like expressions
                scriptPattern = Pattern.compile(
                        "autofocus|FSCommand(.*?)=|on(Abort|Activate|AfterPrint|AfterUpdate|BeforeActivate|BeforeCopy|BeforeCut|BeforeDeactivate|BeforeEditFocus|BeforePaste|BeforePrint|BeforeUnload|BeforeUpdate|Begin|Blur|Bounce|CellChange|Change|Click|ContextMenu|ControlSelect|Copy|DataAvailable|Cut|DataSetChanged|DataSetComplete|DblClick|Deactivate|DragDragEnd|DragLeave|DragEnter|DragOver|DragDrop|DragStart|Drop|End|Error|ErrorUpdate|FilterChange|Finish|Focus|FocusIn|FocusOut|HashChange|Help|Input|KeyDown|KeyPress|KeyUp|LayoutComplete|Load|LoseCapture|MediaComplete|MediaError|Message|MouseDown|MouseEnter|MouseLeave|MouseMove|MouseOut|MouseOver|MouseUp|MouseWheel|Move|MoveEnd|MoveStart|Offline|line|OutOfSync|Paste|Pause|PopState|Progress|PropertyChange|ReadyStateChange|Redo|Repeat|Reset|Resize|ResizeEnd|ResizeStart|Resume|Reverse|RowsEnter|RowExit|RowDelete|RowInserted|Scroll|Seek|Select|SelectiChange|SelectStart|Start|Stop|Storage|SyncRestored|Submit|TimeError|TrackChange|Undo|Unload|URLFlip)(.*?)=|seekSegmentTime",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");

                value = value.replaceAll("<", "&lt;");
                value = value.replaceAll(">", "&gt;");
            }

            if("HIGH".equals(this.filterLevel)) {
                value = value.replaceAll("!", "&#33;");
                value = value.replaceAll("\"", "&#34;");
                value = value.replaceAll("\'", "&#39;");
                value = value.replaceAll("\\+", "&#43;");
                value = value.replaceAll("=", "&#61;");
            }
        }
        return value;
    }
}