package com.bh.subject.std.common.service.cache.operations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * RedisOperations
 * 
 * @author BH Jun
 */
public interface RedisOperations {

    /**
     * Hash Operations
     */

    public boolean hasKey(String bucketId, String key);

    public String getData(String bucketId, String key);

    public List<String> getMultiData(String bucketId, List<String> keys);

    public String setData(String bucketId, String key, String value);

    public String setMultiData(String bucketId, Map<String, String> keyValueMap);

    public String deleteData(String bucketId, String key);

    public String deleteBucket(String bucketId);

    public String setBucketExpireTime(String bucketId, long expireTime);

    public void setIndex(String indexKey, String bucketId);

    public void removeIndex(String indexKey);

    public Set<String> getIndex(String indexKey);

    public Boolean isIndex(String indexKey, String bucketId);

    public Set<String> getKeys(String pattern);

    public Set<String> getHashKeys(String bucketId, String pattern);

    public Set<Entry<String, String>> getHashDataMap(String bucketId, String pattern);
}
