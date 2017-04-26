package com.bh.subject.std.common.service.cache;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bh.subject.std.common.service.cache.operations.RedisOperations;
import com.bh.subject.std.common.service.cache.type.bucket.BucketType;
import com.bh.subject.std.common.service.cache.type.key.UserCacheKeyType;

import net.sf.json.JSONObject;

/**
 * GMS API Service에서 User 단위로 사용하는 Redis Data를 입력(or 갱신)/삭제/반환 하는 기능을 제공하는
 * Class.
 * 
 * @author BH Jun
 */
@Component("UserCacheAccessor")
public class UserCacheAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheAccessor.class);
    //    private static final String USER_PROC_TREE_STATUS_NAME = UserCacheValueKey.USER_PROC_TREE_STATUS_NAME.getValueKey();
    //    private static final String USER_OBJ_TREE_STATUS_NAME = UserCacheValueKey.USER_OBJ_TREE_STATUS_NAME.getValueKey();

    @Autowired
    private RedisOperations redisOperations;

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getUserProcTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        String procTreeStatusStr = redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.PROC_TREE_STATUS.getKeyName(userId, parentId));

        LOGGER.debug("getUserProcTreeStatus : " + procTreeStatusStr);

        return JSONObject.fromObject(procTreeStatusStr);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @param procTreeStatus
     *            JSONObject
     * @throws Exception
     */
    public void setUserProcTreeStatus(String tenantId, String userId, String parentId, JSONObject procTreeStatus) throws Exception {
        LOGGER.debug("setUserProcTreeStatus : " + procTreeStatus.toString());

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.PROC_TREE_STATUS.getKeyName(userId, parentId),
                procTreeStatus.toString());
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @throws Exception
     */
    public void removeUserProcTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.PROC_TREE_STATUS.getKeyName(userId, parentId));
    }

    /**
     * @param tenantId
     * @param userId
     * @param parentId
     * @return
     * @throws Exception
     */
    public JSONObject getUserIsoIntroProcTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        String procTreeStatusStr = redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.ISO_INTRO_PROC_TREE_STATUS.getKeyName(userId, parentId));

        LOGGER.debug("getUserProcTreeStatus : " + procTreeStatusStr);

        return JSONObject.fromObject(procTreeStatusStr);
    }

    /**
     * @param tenantId
     * @param userId
     * @param parentId
     * @param procTreeStatus
     * @throws Exception
     */
    public void setUserIsoIntroProcTreeStatus(String tenantId, String userId, String parentId, JSONObject procTreeStatus) throws Exception {
        LOGGER.debug("setUserProcTreeStatus : " + procTreeStatus.toString());

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.ISO_INTRO_PROC_TREE_STATUS.getKeyName(userId, parentId), procTreeStatus.toString());
    }

    /**
     * @param tenantId
     * @param userId
     * @param parentId
     * @throws Exception
     */
    public void removeUserIsoIntroProcTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.ISO_INTRO_PROC_TREE_STATUS.getKeyName(userId, parentId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @return objTreeStatus
     * @throws Exception
     */
    public JSONObject getUserOrgTreeStatus(String tenantId, String userId, String parentId) throws Exception {

        String objTreeStatusStr = redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.ORG_TREE_STATUS.getKeyName(userId, parentId));

        LOGGER.debug("getUserProcTreeStatus : " + objTreeStatusStr);

        return JSONObject.fromObject(objTreeStatusStr);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @param objTreeStatus
     *            List<UserTreeStatusVO>
     * @throws Exception
     */
    public void setUserObjTreeStatus(String tenantId, String userId, String parentId, JSONObject objTreeStatus) throws Exception {

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.OBJ_TREE_STATUS.getKeyName(userId, parentId),
                objTreeStatus.toString());
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @throws Exception
     */
    public void removeUserObjTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.OBJ_TREE_STATUS.getKeyName(userId, parentId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @return objTreeStatus
     * @throws Exception
     */
    public JSONObject getUserObjTreeStatus(String tenantId, String userId, String parentId) throws Exception {

        String objTreeStatusStr = redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.OBJ_TREE_STATUS.getKeyName(userId, parentId));

        LOGGER.debug("getUserProcTreeStatus : " + objTreeStatusStr);

        return JSONObject.fromObject(objTreeStatusStr);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @param objTreeStatus
     *            List<UserTreeStatusVO>
     * @throws Exception
     */
    public void setUserOrgTreeStatus(String tenantId, String userId, String parentId, JSONObject orgTreeStatus) throws Exception {

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.ORG_TREE_STATUS.getKeyName(userId, parentId),
                orgTreeStatus.toString());
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param parentId
     *            String
     * @throws Exception
     */
    public void removeUserOrgTreeStatus(String tenantId, String userId, String parentId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.ORG_TREE_STATUS.getKeyName(userId, parentId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param menuId
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getUserTmpMenuSaveData(String tenantId, String userId, String menuId) throws Exception {
        String userTmpDataStr = redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.TMP_MENU_SAVE_DATA.getKeyName(userId, menuId));

        return JSONObject.fromObject(userTmpDataStr);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param menuId
     *            String
     * @param paramVo
     *            T
     * @throws Exception
     */
    public <T> void setUserTmpMenuSaveData(String tenantId, String userId, String menuId, T paramVo) throws Exception {
        JSONObject obj = JSONObject.fromObject(paramVo);

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.TMP_MENU_SAVE_DATA.getKeyName(userId, menuId),
                obj.toString());
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param menuId
     *            String
     * @throws Exception
     */
    public void removeUserTmpMenuSaveData(String tenantId, String userId, String menuId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.TMP_MENU_SAVE_DATA.getKeyName(userId, menuId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public String getUserModelerStatus(String tenantId, String userId) throws Exception {
        return redisOperations.getData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.MODELER_STATUS.getKeyName(userId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param statusVal
     *            String
     * @throws Exception
     */
    public void setUserModelerStatus(String tenantId, String userId, String statusVal) throws Exception {
        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.MODELER_STATUS.getKeyName(userId), statusVal);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @throws Exception
     */
    public void removeUserModelerStatus(String tenantId, String userId) throws Exception {
        redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId), UserCacheKeyType.MODELER_STATUS.getKeyName(userId));
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param dataType
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public Object getUserModelerProcData(String tenantId, String userId, String dataType, Class<?> beanClass) throws Exception {
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.MODELER_PROC_DATA.getKeyName(userId, dataType);
        Object result = null;

        if(redisOperations.hasKey(bucketId, keyName)) {
            String modelerProcDataStr = redisOperations.getData(bucketId, keyName);

            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(modelerProcDataStr, beanClass);
        }

        return result;
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param dataType
     *            String
     * @param paramVo
     *            T
     * @throws Exception
     */
    public <T> void setUserModelerProcData(String tenantId, String userId, String dataType, T paramVo) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(paramVo);

        redisOperations.setData(BucketType.USER_CACHE.getBucketId(tenantId),
                UserCacheKeyType.MODELER_PROC_DATA.getKeyName(userId, dataType), jsonString);
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param dataTypePrefix
     *            String
     * @throws Exception
     */
    public void removeUserModelerProcData(String tenantId, String userId, String dataTypePrefix) throws Exception {
        String keyPrefix = UserCacheKeyType.MODELER_PROC_DATA.getKeyName(userId, dataTypePrefix);
        Set<String> keys = redisOperations.getHashKeys(BucketType.USER_CACHE.getBucketId(tenantId), keyPrefix + "*");
        for(String key : keys) {
            redisOperations.deleteData(BucketType.USER_CACHE.getBucketId(tenantId), key);
        }
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getUserTmpFileInfo(String tenantId, String userId, String tmpFileKey) throws Exception {
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.TMP_FILE_INFO.getKeyName(userId, tmpFileKey);
        JSONObject result = null;

        if(redisOperations.hasKey(bucketId, keyName)) {
            result = JSONObject.fromObject(redisOperations.getData(bucketId, keyName));
        }
        return result;
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @param statusVal
     *            String
     * @throws Exception
     */

    public <T> void setUserTmpFileInfo(String tenantId, String userId, String tmpFileKey, T param) throws Exception {
        JSONObject obj = JSONObject.fromObject(param);
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.TMP_FILE_INFO.getKeyName(userId, tmpFileKey);

        redisOperations.setData(bucketId, keyName, obj.toString());
    }

    /**
     * @param tenantId
     *            String
     * @param userId
     *            String
     * @throws Exception
     */
    public void removeUserTmpFileInfo(String tenantId, String userId, String tmpFileKey) throws Exception {
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.TMP_FILE_INFO.getKeyName(userId, tmpFileKey);

        redisOperations.deleteData(bucketId, keyName);
    }

    public <T> void setUserProcValidResult(String tenantId, String userId, String procId, T validResult) throws Exception {
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.MODELER_VALID_RESULT.getKeyName(userId, procId);
        JSONObject result = JSONObject.fromObject(validResult);

        redisOperations.setData(bucketId, keyName, result.toString());
    }

    public JSONObject getUserProcValidResult(String tenantId, String userId, String procId) throws Exception {
        String bucketId = BucketType.USER_CACHE.getBucketId(tenantId);
        String keyName = UserCacheKeyType.MODELER_VALID_RESULT.getKeyName(userId, procId);
        JSONObject result = null;

        if(redisOperations.hasKey(bucketId, keyName)) {
            result = JSONObject.fromObject(redisOperations.getData(bucketId, keyName));
            //            redisOperations.deleteData(bucketId, keyName);
        }

        return result;
    }
}
