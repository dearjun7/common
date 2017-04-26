package com.bh.subject.std.common.error;

/**
 * Error code interface.
 * 
 * @author BH Jun
 */
public interface ErrorCode {

    public String getErrorCode();

    public default int getResponseCode() {
        return Integer.parseInt(this.getErrorCode().split("-")[1]);
    }
}
