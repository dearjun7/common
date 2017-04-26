package com.bh.subject.std.common.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.subject.std.common.error.CommonErrorCode;
import com.bh.subject.std.common.error.GMSException;
import com.bh.subject.std.common.service.cache.operations.RedisOperations;
import com.bh.subject.std.common.service.cache.type.bucket.BucketType;
import com.bh.subject.std.common.service.cache.type.key.LoginFailKeyType;

/**
 * LoginFailAccessor.
 * 
 * @author BH Jun
 */
@Component
public class LoginFailAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFailAccessor.class);

    @Autowired
    private RedisOperations redisOperations;

    public int getLoginFailCount(String userEmail) {
        int loginFailCount = 0;

        try {
            loginFailCount = Integer.parseInt(
                    redisOperations.getData(BucketType.LOGIN_FAIL.getBucketId(), LoginFailKeyType.LOGIN_FAIL.getKeyName(userEmail)));

            if(loginFailCount > 0) {
                LOGGER.info("User(" + userEmail + ") Login Attempt Count - " + loginFailCount);
            }

        } catch(GMSException e) {
            if(e.getErrorCode().equals(CommonErrorCode.NO_DATA_FOUND)) {
                loginFailCount = 0;
            } else {
                throw new GMSException(e);
            }
        }

        return loginFailCount;
    }

    public void setLoginFailCount(String userEmail) {
        int loginFailCount = this.getLoginFailCount(userEmail) + 1;

        redisOperations.setData(BucketType.LOGIN_FAIL.getBucketId(), LoginFailKeyType.LOGIN_FAIL.getKeyName(userEmail),
                String.valueOf(loginFailCount));
    }

    public void removeLoginFailCount(String userEmail) {
        redisOperations.deleteData(BucketType.LOGIN_FAIL.getBucketId(), LoginFailKeyType.LOGIN_FAIL.getKeyName(userEmail));
    }
}
