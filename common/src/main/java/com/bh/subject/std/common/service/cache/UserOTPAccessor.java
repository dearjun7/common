package com.bh.subject.std.common.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.subject.std.common.service.cache.operations.RedisOperations;
import com.bh.subject.std.common.service.cache.type.bucket.BucketType;
import com.bh.subject.std.common.service.cache.type.key.UserOTPKeyType;

/**
 * UserOTPAccessor
 * 
 * @author BH Jun
 */

@Component
public class UserOTPAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOTPAccessor.class);
    private static final String USER_SESSION_BUCKET = UserOTPKeyType.USER_SESSION_BUCKET.getKeyName();

    @Autowired
    private RedisOperations redisOperations;

    public boolean hasAccessOTP(String accessOTP) {
        String bucketId = this.getOTPBucketId(accessOTP);
        return redisOperations.hasKey(bucketId, USER_SESSION_BUCKET);
    }

    public String getUserSessionBucketId(String accessOTP) {
        String bucketId = this.getOTPBucketId(accessOTP);

        return redisOperations.getData(bucketId, USER_SESSION_BUCKET);
    }

    public void setAccessOTP(String accessOTP, String userSessionBucketId) {
        String bucketId = this.getOTPBucketId(accessOTP);
        redisOperations.setData(bucketId, USER_SESSION_BUCKET, userSessionBucketId);
    }

    public void setExpireTimeAccessOTP(String accessOTP, long expireTime) {
        String bucketId = this.getOTPBucketId(accessOTP);
        redisOperations.setBucketExpireTime(bucketId, expireTime);
    }

    public void removeAccessOTP(String accessOTP) throws Exception {
        String bucketId = this.getOTPBucketId(accessOTP);
        redisOperations.deleteBucket(bucketId);
    }

    private String getOTPBucketId(String accessOTP) {
        return BucketType.USER_OTP.getBucketId(accessOTP);
    }
}
