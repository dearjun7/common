package com.bh.subject.std.common.log;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpMethod;

import com.bh.subject.std.common.service.http.HttpUtil;
import com.bh.subject.std.common.service.http.core.GMSApplicationHttpRequest;

import net.sf.json.JSONObject;

public class MDCFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        String requestMethod = httpRequest.getMethod();
        String remoteIP = HttpUtil.getClientIp(httpRequest);
        String queryString = "".equals(httpRequest.getQueryString()) || httpRequest.getQueryString() == null ? ""
                : "?" + httpRequest.getQueryString();
        String requestURI = httpRequest.getRequestURI() + queryString;
        String body = null;
        String parameters = "";
        JSONObject headers = JSONObject.fromObject(this.getHeadersInfo(httpRequest));

        if(!requestMethod.equals(HttpMethod.GET.toString())) {
            String contentType = httpRequest.getContentType() == null || "".equals(httpRequest.getContentType()) ? ""
                    : httpRequest.getContentType().toLowerCase();

            if(!contentType.startsWith("multipart/form-data")) {
                GMSApplicationHttpRequest requestWrapper = new GMSApplicationHttpRequest((HttpServletRequest) request);
                body = this.getBody(requestWrapper);
                httpRequest = requestWrapper;
            }
        }

        int index = 0;

        for(String name : Collections.<String>list(httpRequest.getParameterNames())) {
            String value = httpRequest.getParameter(name);

            if(index != 0) {
                parameters += "&";
            }

            parameters += name + "=" + value;
            index++;
        }

        MDC.put("remoteIP", remoteIP);
        MDC.put("requestURI", requestURI);
        MDC.put("requestMethod", requestMethod);
        MDC.put("headers", headers.toString());
        MDC.put("parameters", "".equals(parameters) ? "none" : parameters);
        MDC.put("body", body);

        try {
            chain.doFilter(httpRequest, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {

    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private String getBody(GMSApplicationHttpRequest requestWrapper) throws IOException {
        //        StringBuilder buffer = new StringBuilder();
        //        BufferedReader reader = requestWrapper.getReader();
        //        String line;
        //        while((line = reader.readLine()) != null) {
        //            buffer.append(line);
        //        }
        //
        //        return buffer.toString().length() == 0 ? "none" : buffer.toString();

        return requestWrapper.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
