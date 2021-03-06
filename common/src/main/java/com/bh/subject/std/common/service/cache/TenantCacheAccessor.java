package com.bh.subject.std.common.service.cache;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.subject.std.common.datasource.type.DataSourceType;
import com.bh.subject.std.common.error.CommonErrorCode;
import com.bh.subject.std.common.error.GMSException;
import com.bh.subject.std.common.service.cache.operations.RedisOperations;
import com.bh.subject.std.common.service.cache.type.bucket.BucketType;
import com.bh.subject.std.common.service.cache.type.key.TenantCacheKeyType;
import com.bh.subject.std.common.service.cache.valuekey.TenantCacheValueKey;
import com.bh.subject.std.common.util.JSONConverter;

import net.sf.json.JSONObject;

/**
 * GMS API Service에서 Tenant 단위로 사용하는 Redis Data를 입력(or 갱신)/삭제/반환 하는 기능을 제공하는
 * Class.
 * 
 * @author BH Jun
 */
@Component
public class TenantCacheAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantCacheAccessor.class);

    public static final String COMMON_CODE_LIST_NAME = TenantCacheValueKey.COMMON_CODE_LIST_NAME.getValueKey();
    public static final String GLOBAL_OPTION_LIST_NAME = TenantCacheValueKey.GLOBAL_OPTION_LIST_NAME.getValueKey();

    @Autowired
    private RedisOperations redisOperations;

    /**
     * Tenant가 계약한 Database Type(Oracle, MySQL) 데이터를 입력.
     * 
     * @param tenantId
     *            String
     * @param dataSourceType
     *            DataSourceType
     * @return String
     * @throws Exception
     */
    public void setTenantDatasource(String tenantId, DataSourceType dataSourceType) throws Exception {
        LOGGER.debug("setTenantDatasource start : " + tenantId + ", " + dataSourceType);

        redisOperations.setData(BucketType.TENANT_CACHE.getBucketId(tenantId), TenantCacheKeyType.DATASOURCE.getKeyName(),
                dataSourceType.toString());
    }

    /**
     * Tenant가 계약한 Database Type(Oracle, MySQL) 데이터를 조회.
     * 
     * @param tenantId
     *            String
     * @return String
     * @throws Exception
     */
    public String getTenantDatasource(String tenantId) throws Exception {
        LOGGER.debug("getTenantDatasource start : " + tenantId);
        String tenantDataSource = null;

        try {
            tenantDataSource = redisOperations.getData(BucketType.TENANT_CACHE.getBucketId(tenantId),
                    TenantCacheKeyType.DATASOURCE.getKeyName());
        } catch(GMSException e) {
            if(CommonErrorCode.NO_DATA_FOUND.equals(e.getErrorCode())) {
                tenantDataSource = "MARIADB";

                return tenantDataSource;
            } else {
                throw new GMSException(e);
            }
        }

        return tenantDataSource;
    }

    /**
     * @param tenantId
     *            String
     * @param option
     *            List<GlobalOptionVO>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setTenantServiceConfigData(String tenantId, List option) throws Exception {
        JSONObject obj = JSONConverter.listToJsonObject(option, GLOBAL_OPTION_LIST_NAME);

        redisOperations.setData(BucketType.TENANT_CACHE.getBucketId(tenantId), TenantCacheKeyType.GLOBAL_OPTION.getKeyName(),
                obj.toString());
    }

    /**
     * @param tenantId
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getTenantServiceConfigData(String tenantId) throws Exception {
        String globalOptionStr = redisOperations.getData(BucketType.TENANT_CACHE.getBucketId(tenantId),
                TenantCacheKeyType.GLOBAL_OPTION.getKeyName());

        return JSONObject.fromObject(globalOptionStr);
    }

    /**
     * @param tenantId
     *            String
     * @param upperCode
     *            String
     * @param param
     *            List
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setTenantCommonCodeList(String tenantId, String upperCode, List param) throws Exception {
        if(param.size() > 0) {
            JSONObject obj = JSONConverter.listToJsonObject(param, COMMON_CODE_LIST_NAME);

            redisOperations.setData(BucketType.TENANT_CACHE.getBucketId(tenantId), TenantCacheKeyType.COMMON_CODE.getKeyName(upperCode),
                    obj.toString());
        }
    }

    /**
     * @param tenantId
     *            String
     * @param upperCode
     *            String
     * @param param
     *            Map
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void setTenantCommonCodeList(String tenantId, String upperCode, Map param) throws Exception {
        if(!param.isEmpty()) {
            JSONObject obj = JSONObject.fromObject(param);

            redisOperations.setData(BucketType.TENANT_CACHE.getBucketId(tenantId), TenantCacheKeyType.COMMON_CODE.getKeyName(upperCode),
                    obj.toString());
        }
    }

    /**
     * @param tenantId
     *            String
     * @param upperCode
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getTenantCommonCodeList(String tenantId, String upperCode) throws Exception {
        String commonCodeStr = redisOperations.getData(BucketType.TENANT_CACHE.getBucketId(tenantId),
                TenantCacheKeyType.COMMON_CODE.getKeyName(upperCode));

        return JSONObject.fromObject(commonCodeStr);
    }

    /**
     * @param tenantId
     *            String
     * @param upperCode
     *            String
     * @throws Exception
     */
    public void removeTenantCommonCodeList(String tenantId, String upperCode) throws Exception {
        redisOperations.deleteData(BucketType.TENANT_CACHE.getBucketId(tenantId), TenantCacheKeyType.COMMON_CODE.getKeyName(upperCode));
    }
}
