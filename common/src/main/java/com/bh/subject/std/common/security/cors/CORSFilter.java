/**
 * Any software product designated as "Handysoft Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of Handysoft CO., LTD (“Handysoft”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for Handysoft’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** Handysoft does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 8. 9. First Draft
 */
package com.bh.subject.std.common.security.cors;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class CORSFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);

    private FilterConfig filterConfig;

    @Value("#{config['gms.common.web.domain']}")
    private String defaultOriginDomain;
    @Value("#{config['gms.common.api.domain']}")
    private String defaultApiDomain;

    @Autowired
    private CORSOriginDAO corsOriginDAO;

    private String apiDomain = null;
    private boolean isAllowDynamicOriginDomain = true;

    private String accessControlAllowMethods;
    private String accessControlMaxAge;
    private String accessControlAllowHeaders;
    private String accessControlAllowOrigin;
    private String accessControlAllowCredentials;

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public void setIsAllowDynamicOriginDomain(boolean isAllowDynamicOriginDomain) {
        this.isAllowDynamicOriginDomain = isAllowDynamicOriginDomain;
    }

    public String getAccessControlAllowMethods() {
        return accessControlAllowMethods;
    }

    public void setAccessControlAllowMethods(String accessControlAllowMethods) {
        this.accessControlAllowMethods = accessControlAllowMethods;
    }

    public String getAccessControlMaxAge() {
        return accessControlMaxAge;
    }

    public void setAccessControlMaxAge(String accessControlMaxAge) {
        this.accessControlMaxAge = accessControlMaxAge;
    }

    public String getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public String getAccessControlAllowOrigin() {
        return accessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public String getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(String accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }

    @PostConstruct
    public void init() {
        if(this.apiDomain == null) {
            this.apiDomain = this.defaultApiDomain;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        if(!isAllowDynamicOriginDomain) {
            this.setCORSHeader(res, accessControlAllowMethods, accessControlMaxAge, accessControlAllowHeaders, accessControlAllowOrigin,
                    accessControlAllowCredentials);
        } else {
            String originDomain = this.getOriginDomain(req);

            LOGGER.debug("api call REFERER : " + originDomain);

            boolean isAllowedCorsOrigin = true;

            if(!originDomain.equals(apiDomain)) {
                isAllowedCorsOrigin = corsOriginDAO.selectIsCORSOrigin(originDomain);
            }

            LOGGER.debug("api call isAllowedCorsOrigin : " + isAllowedCorsOrigin);

            if(isAllowedCorsOrigin) {
                this.setCORSHeader(res, accessControlAllowMethods, accessControlMaxAge, accessControlAllowHeaders, originDomain,
                        accessControlAllowCredentials);
            } else {
                res.sendError(403);
            }
        }

        chain.doFilter(request, response);
    }

    private void setCORSHeader(HttpServletResponse res, String accessControlAllowMethods, String accessControlMaxAge,
            String accessControlAllowHeaders, String accessControlAllowOrigin, String accessControlAllowCredentials) {
        res.setHeader("Access-Control-Allow-Methods", accessControlAllowMethods);
        res.setHeader("Access-Control-Max-Age", accessControlMaxAge);
        res.setHeader("Access-Control-Allow-Headers", accessControlAllowHeaders);
        res.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        res.setHeader("Access-Control-Allow-Credentials", accessControlAllowCredentials);
    }

    private String getOriginDomain(HttpServletRequest request) {
        String result = null;
        String origin = request.getHeader("Origin");

        if("OPTIONS".equals(request.getMethod())) {
            result = origin;
        } else {
            String referer = request.getHeader("referer");
            String tmpResult = null;

            if(origin == null || "".equals(origin)) {
                tmpResult = referer != null && !"".equals(referer) ? referer.substring(0, referer.indexOf("/", 8)) : null;
            } else {
                tmpResult = origin;
            }

            result = tmpResult == null || "".equals(tmpResult) ? defaultOriginDomain : tmpResult;
        }

        return result;
    }
}
