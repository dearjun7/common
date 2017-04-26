package com.bh.subject.std.common.error.resolver;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.bh.subject.std.common.error.CommonErrorCode;
import com.bh.subject.std.common.error.ErrorCode;
import com.bh.subject.std.common.error.GMSException;
import com.bh.subject.std.common.response.ErrorResponseVO;

/**
 * GMS 프로젝트 공통 Exception 처리 resolver.
 * 
 * @author BH Jun
 */
public class GMSExceptionResolver implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMSExceptionResolver.class);

    @Autowired
    @Qualifier("messageSource")
    private MessageSource msgSource;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mav = new ModelAndView();
        ErrorResponseVO result = new ErrorResponseVO();
        Locale locale = (Locale) request.getAttribute(CookieLocaleResolver.class.getName() + ".LOCALE");
        String requestURI = request.getRequestURI();

        LOGGER.debug(" kind of Exception = " + ex.getClass());
        LOGGER.debug(" cause of Exception = " + ex.getCause());
        LOGGER.debug(" Place of Exception = " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName()
                + "(Line : " + ex.getStackTrace()[0].getLineNumber() + ")");

        if(ex instanceof GMSException) {
            GMSException exception = (GMSException) ex;

            ErrorCode errorObject = exception.getErrorCode();
            Object[] params = exception.getErrorParams();
            List<ConcurrentHashMap<String, String>> errorDataList = exception.getErrDataList();

            if(errorObject == null) {
                errorObject = CommonErrorCode.INTERNAL_SERVER_ERROR;
            }

            LOGGER.debug(" Exception errorObject = " + errorObject);
            LOGGER.debug(" Exception params size = " + (params != null ? params.length : 0));
            LOGGER.debug(" Exception locale = " + locale);

            if(exception.getMessage() == null) {
                exception.setMessage(msgSource.getMessage(errorObject.getErrorCode(), params, new Locale(locale.getLanguage())));
            }

            response.setStatus(errorObject.getResponseCode());

            result.getHeader().setStatus(response.getStatus());
            result.getBody().getDocs().setErrCode(errorObject.getErrorCode());
            result.getBody().getDocs().setErrMessage(exception.getMessage());
            result.getBody().getDocs().setErrDataList(errorDataList);

            mav.addObject("response", result);
        } else {
            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            String errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();

            response.setStatus(status);

            result.getHeader().setStatus(status);
            result.getBody().getDocs().setErrCode(errorCode);
            result.getBody().getDocs().setErrMessage(msgSource.getMessage(errorCode, null, locale));

            mav.addObject("response", result);
        }

        // http status가 4xx이 아닌 모든 에러에 대해서는 Error stack trace를 출력한다 
        if(!Pattern.matches("4[0-9]{2}|3[0-9]{2}", String.valueOf(response.getStatus()))) {
            LOGGER.error("Occurred Exception", ex);
        } else {
            if(LOGGER.isDebugEnabled()) { // LOGGER가 디버그 모드이면 stact trace 출력
                LOGGER.error("Occurred Exception", ex);
            } else {
                Throwable cause = this.getCause(ex);

                LOGGER.error("Occurred Exception" + ex.getMessage() + ", Cause By : " + cause + ", RequestURI : " + requestURI);
            }
        }

        return mav;
    }

    private Throwable getCause(Throwable ex) {
        Throwable cause = ex.getCause();

        if(cause != null) {
            if(cause instanceof GMSException) {
                cause = cause.getCause() == null ? cause : cause.getCause();
            }
        } else {
            cause = ex;
        }

        return cause;
    }
}
