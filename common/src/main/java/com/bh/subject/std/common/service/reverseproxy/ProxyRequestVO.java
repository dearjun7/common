package com.bh.subject.std.common.service.reverseproxy;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

public class ProxyRequestVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6286747657849586376L;

    private HttpRequestBase httpRequestBase;
    private HttpEntity proxyParams;
    private Header[] headers;
    private String traceId;
    private List<File> tmpFileList;
    private String requestMethod;

    public HttpRequestBase getHttpRequestBase() {
        return httpRequestBase;
    }

    public void setHttpRequestBase(HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }

    public HttpEntity getProxyParams() {
        return proxyParams;
    }

    public void setProxyParams(HttpEntity proxyParams) {
        this.proxyParams = proxyParams;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<File> getTmpFileList() {
        return tmpFileList;
    }

    public void setTmpFileList(List<File> tmpFileList) {
        this.tmpFileList = tmpFileList;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}
