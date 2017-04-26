package com.bh.subject.std.common.service.i18n;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

public class GMSLocaleChangeInterceptor extends LocaleChangeInterceptor {

    @Override
    public void setParamName(String paramName) {
        super.setParamName(paramName);
    }

    @Override
    public String getParamName() {
        return super.getParamName();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        String reqLocaleStr = request.getParameter(super.getParamName());
        String newLocale = "";

        if(reqLocaleStr != null) {
            newLocale = reqLocaleStr;

            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if(localeResolver == null) {
                throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
            }
            localeResolver.setLocale(request, response, new Locale(newLocale));
        }
        // Proceed in any case.
        return true;
    }
}
