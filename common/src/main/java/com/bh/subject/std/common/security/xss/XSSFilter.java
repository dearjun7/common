package com.bh.subject.std.common.security.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.subject.std.common.service.http.core.XSSHttpRequest;

/**
 * XSS(Cross Site Script) 공격을 방지하기 위한 Filter 클래스.
 * 
 * @author BH Jun
 * @version
 */
public class XSSFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XSSFilter.class);

    /**
     * 이 Filter의 FilterConfig
     */
    private FilterConfig filterConfig;

    /**
     * Filter Level.
     */
    private String filterLevel;

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        this.filterLevel = filterConfig.getInitParameter("level");

        // 사용 가능한 레벨인 'HIGH' 나 'LOW'로 입력되지 않으면, 경고 처리 후, 'HIGH'를 사용.
        if(!"HIGH".equalsIgnoreCase(this.filterLevel) && !"LOW".equalsIgnoreCase(this.filterLevel)) {
            LOGGER.warn("'{}' level is not allowed XSS-Filter Level. Only 'HIGH' or 'LOW'.", this.filterLevel);
            LOGGER.warn("Changed to Default XSS-Filter Level: HIGH");

            this.filterLevel = "HIGH";
        }

        LOGGER.info("XSS Filter Level is '{}'.", this.filterLevel);
        LOGGER.info("Filter '{}' configured successfully", filterConfig.getFilterName());
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * FilterConfig를 반환한다.
     * 
     * @return the FilterConfig instance 또는 없을 경우, <code>null</code>
     * @see javax.servlet.GenericServlet#getServletConfig()
     */
    public final FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    /**
     * 현재 Filter의 name을 반환한다.
     * 
     * @return the filter name 또는 없을 경우, <code>null</code>.
     * @see javax.servlet.GenericServlet#getServletName()
     * @see javax.servlet.FilterConfig#getFilterName()
     */
    public final String getFilterName() {
        return this.filterConfig != null ? this.filterConfig.getFilterName() : null;
    }

    /**
     * http request 시 XSS Filter 실행한다.
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        XSSHttpRequest xssRequest = new XSSHttpRequest((HttpServletRequest) request);
        xssRequest.setFilterLevel(filterLevel);

        chain.doFilter(xssRequest, response);

    }

    public boolean isMultipartRequest(HttpServletRequest request) {
        String type = request.getHeader("Content-Type");
        return (type != null) && type.startsWith("multipart/form-data");
    }
}
