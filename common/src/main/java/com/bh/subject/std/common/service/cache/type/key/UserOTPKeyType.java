package com.bh.subject.std.common.service.cache.type.key;

/**
 * 새로운 사용자 Session 발급을 위한 임시 OTP Type Class.
 * 
 * @author BH Jun
 */
public enum UserOTPKeyType {
    USER_SESSION_BUCKET {

        @Override
        public String getKeyName() {
            return this.toString();
        }

    };

    public abstract String getKeyName();
}
