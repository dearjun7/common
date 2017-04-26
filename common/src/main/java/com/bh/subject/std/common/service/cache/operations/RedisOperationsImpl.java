package com.bh.subject.std.common.service.cache.operations;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import com.bh.subject.std.common.error.CommonErrorCode;
import com.bh.subject.std.common.error.GMSException;

/**
 * RedisHashOperations
 * 
 * @author BH Jun
 */
@Component
public class RedisOperationsImpl implements RedisOperations {

    protected static final String SUCCEED_MSG = "SUCCEED";
    protected static final String FAILURE_MSG = "FAILURE";

    @Resource(name = "readRedisTemplate")
    private HashOperations<String, String, String> readHashOps;
    @Resource(name = "readRedisTemplate")
    private SetOperations<String, String> readIndexOps;

    @Resource(name = "readRedisTemplate")
    private ListOperations<String, String> readOps;

    @Resource(name = "writeRedisTemplate")
    private HashOperations<String, String, String> writeHashOps;
    @Resource(name = "writeRedisTemplate")
    private SetOperations<String, String> writeIndexOps;

    protected enum DeleteTarget {
        BUCKET {

            @Override
            public String delete(org.springframework.data.redis.core.RedisOperations<String, ?> redisOps, String bucketId, String key) {
                redisOps.delete(bucketId);
                return SUCCEED_MSG;
            }
        },
        DATA {

            @Override
            public String delete(org.springframework.data.redis.core.RedisOperations<String, ?> redisOps, String bucketId, String key) {
                redisOps.opsForHash().delete(bucketId, key);
                return SUCCEED_MSG;
            }
        };

        protected abstract String delete(org.springframework.data.redis.core.RedisOperations<String, ?> redisOps, String bucketId,
                String key);
    }

    @Override
    public boolean hasKey(String bucketId, String key) {
        return readHashOps.hasKey(bucketId, key);
    }

    @Override
    public String getData(String bucketId, String key) {
        String result = readHashOps.get(bucketId, key);

        if(result == null) {
            throw new GMSException(CommonErrorCode.NO_DATA_FOUND);
        }

        return result;
    }

    @Override
    public List<String> getMultiData(String bucketId, List<String> keys) {
        List<String> result = readHashOps.multiGet(bucketId, keys);

        if(result == null || result.size() == 0) {
            throw new GMSException(CommonErrorCode.NO_DATA_FOUND);
        }

        return result;
    }

    @Override
    public String setData(String bucketId, String key, String value) {
        writeHashOps.put(bucketId, key, value);

        return SUCCEED_MSG;
    }

    @Override
    public String setMultiData(String bucketId, Map<String, String> keyValueMap) {
        writeHashOps.putAll(bucketId, keyValueMap);

        return SUCCEED_MSG;
    }

    @Override
    public String deleteData(String bucketId, String key) {
        return DeleteTarget.DATA.delete(writeHashOps.getOperations(), bucketId, key);
    }

    @Override
    public String deleteBucket(String bucketId) {
        return DeleteTarget.BUCKET.delete(writeHashOps.getOperations(), bucketId, null);
    }

    @Override
    public String setBucketExpireTime(String bucketId, long expireTime) {
        return writeHashOps.getOperations().expire(bucketId, expireTime, TimeUnit.SECONDS) == true ? SUCCEED_MSG : FAILURE_MSG;
    }

    @Override
    public void setIndex(String indexKey, String bucketId) {
        writeIndexOps.add(indexKey, bucketId);
    }

    @Override
    public void removeIndex(String indexKey) {
        Iterator<String> iter = this.getIndex(indexKey).iterator();

        while(iter.hasNext()) {
            writeIndexOps.remove(indexKey, iter.next());
        }
    }

    @Override
    public Set<String> getIndex(String indexKey) {
        return readIndexOps.members(indexKey);
    }

    @Override
    public Boolean isIndex(String indexKey, String bucketId) {
        return readIndexOps.isMember(indexKey, bucketId);
    }

    @Override
    public Set<String> getKeys(String pattern) {
        Set<String> result = readIndexOps.getOperations().execute(new RedisCallback<Set<String>>() {

            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                Set<String> keys = new HashSet<String>();
                ScanOptions scanOptions = new ScanOptions.ScanOptionsBuilder().match(pattern).build();
                Cursor<byte[]> cursor = connection.scan(scanOptions);

                while(cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }

                try {
                    cursor.close();
                } catch(IOException e) {}

                return keys;
            }
        });

        return result;
    }

    @Override
    public Set<String> getHashKeys(String bucketId, String pattern) {
        Set<String> result = readIndexOps.getOperations().execute(new RedisCallback<Set<String>>() {

            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                Set<String> keys = new HashSet<String>();
                ScanOptions scanOptions = new ScanOptions.ScanOptionsBuilder().match(pattern).build();
                Cursor<Entry<byte[], byte[]>> cursor = connection.hScan(bucketId.getBytes(), scanOptions);

                while(cursor.hasNext()) {
                    Entry<byte[], byte[]> entry = cursor.next();
                    byte[] keyByte = entry.getKey();

                    keys.add(new String(keyByte));
                }

                try {
                    cursor.close();
                } catch(IOException e) {}

                return keys;
            }
        });

        return result;
    }

    @Override
    public Set<Entry<String, String>> getHashDataMap(String bucketId, String pattern) {
        Set<Entry<String, String>> result = readIndexOps.getOperations().execute(new RedisCallback<Set<Entry<String, String>>>() {

            @Override
            public Set<Entry<String, String>> doInRedis(RedisConnection connection) throws DataAccessException {
                Map<String, String> resultMap = new HashMap<String, String>();
                ScanOptions scanOptions = new ScanOptions.ScanOptionsBuilder().match(pattern).build();
                Cursor<Entry<byte[], byte[]>> cursor = connection.hScan(bucketId.getBytes(), scanOptions);

                while(cursor.hasNext()) {
                    Entry<byte[], byte[]> entry = cursor.next();
                    String key = new String(entry.getKey());
                    String value = new String(entry.getValue());

                    resultMap.put(key, value);
                }

                try {
                    cursor.close();
                } catch(IOException e) {}

                return resultMap.entrySet();
            }
        });

        return result;
    }
}
