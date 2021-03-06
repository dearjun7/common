package com.bh.subject.std.common.error;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.validation.FieldError;

/**
 * GMSException
 * 
 * @author BH Jun
 */
public class GMSException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1077078834392922877L;

    private ErrorCode errorCode;
    private Object[] errorParams;
    private String message;
    private List<ConcurrentHashMap<String, String>> errDataList;

    public GMSException(ErrorCode errorCode) {
        super();

        this.errorCode = errorCode;
    }

    public GMSException(FieldError fieldError) {
        super();

        this.errorCode = CommonErrorCode.BAD_REQUEST;
        this.message = fieldError.getField() + " - " + fieldError.getDefaultMessage();
    }

    public GMSException(String message, ErrorCode errorCode) {
        super();

        this.errorCode = errorCode;
        this.message = message;
    }

    public GMSException(ErrorCode errorCode, String errorParam) {
        super();

        this.errorCode = errorCode;
        this.errorParams = new Object[1];
        errorParams[0] = errorParam;
    }

    public GMSException(ErrorCode errorCode, String[] errorParams) {
        super();

        this.errorCode = errorCode;
        this.errorParams = errorParams;
    }

    public GMSException(Throwable e, ErrorCode errorCode) {
        super(e);

        this.errorCode = errorCode;
    }

    public GMSException(Throwable e, ErrorCode errorCode, String errorParam) {
        super(e);

        this.errorCode = errorCode;
        this.errorParams = new Object[1];

        errorParams[0] = errorParam;
    }

    public GMSException(Throwable e, ErrorCode errorCode, String[] errorParams) {
        super(e);

        this.errorCode = errorCode;
        this.errorParams = errorParams;
    }

    public GMSException(ErrorCode errorCode, List<ConcurrentHashMap<String, String>> errDataList) {
        super();

        this.errorCode = errorCode;
        this.errDataList = errDataList;
    }

    public GMSException(String message, ErrorCode errorCode, List<ConcurrentHashMap<String, String>> errDataList) {
        super();

        this.errorCode = errorCode;
        this.message = message;
        this.errDataList = errDataList;
    }

    public GMSException(ErrorCode errorCode, String errorParam, List<ConcurrentHashMap<String, String>> errDataList) {
        super();

        this.errorCode = errorCode;
        this.errorParams = new Object[1];
        this.errorParams[0] = errorParam;
        this.errDataList = errDataList;
    }

    public GMSException(ErrorCode errorCode, String[] errorParams, List<ConcurrentHashMap<String, String>> errDataList) {
        super();

        this.errorCode = errorCode;
        this.errorParams = errorParams;
        this.errDataList = errDataList;
    }

    public GMSException(Throwable e, ErrorCode errorCode, List<ConcurrentHashMap<String, String>> errDataList) {
        super(e);

        this.errorCode = errorCode;
        this.message = e.getMessage();
        this.errDataList = errDataList;
    }

    public GMSException(Throwable e, ErrorCode errorCode, String errorParam, List<ConcurrentHashMap<String, String>> errDataList) {
        super(e);

        this.errorCode = errorCode;
        this.message = e.getMessage();
        this.errorParams = new Object[1];
        this.errorParams[0] = errorParam;
        this.errDataList = errDataList;
    }

    public GMSException(Throwable e, ErrorCode errorCode, String[] errorParams, List<ConcurrentHashMap<String, String>> errDataList) {
        super(e);

        this.errorCode = errorCode;
        this.errorParams = errorParams;
        this.message = e.getMessage();
        this.errDataList = errDataList;
    }

    public GMSException(GMSException e) {
        super(e);

        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
        this.errorParams = e.getErrorParams();
        this.errDataList = e.getErrDataList();
    }

    public GMSException(Throwable e) {
        super(e);

        if(e instanceof GMSException) {
            GMSException exception = (GMSException) e;

            this.errorCode = exception.getErrorCode();
            this.message = exception.getMessage();
            this.errorParams = exception.getErrorParams();
            this.errDataList = exception.getErrDataList();
        }
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getErrorParams() {
        return errorParams;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ConcurrentHashMap<String, String>> getErrDataList() {
        return errDataList;
    }

    public void setErrDataList(List<ConcurrentHashMap<String, String>> errDataList) {
        this.errDataList = errDataList;
    }
}
