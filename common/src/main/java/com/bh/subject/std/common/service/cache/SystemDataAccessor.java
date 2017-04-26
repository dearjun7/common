package com.bh.subject.std.common.service.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.subject.std.common.service.cache.operations.RedisOperations;
import com.bh.subject.std.common.service.cache.type.bucket.BucketType;
import com.bh.subject.std.common.service.cache.type.key.SystemDataKeyType;
import com.bh.subject.std.common.service.cache.valuekey.SystemDataValueKey;
import com.bh.subject.std.common.util.JSONConverter;

import net.sf.json.JSONObject;

/**
 * GMS API Service에서 모든 Tenant들이 전역적으로 사용하는 Redis Data를 입력(or 갱신)/삭제/반환 하는 기능을
 * 제공하는 Class.
 * 
 * @author BH Jun
 */
@Component
public class SystemDataAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemDataAccessor.class);
    private static final String SECRET_KEY_TYPE = SystemDataKeyType.TOKEN_SECRET_KEY.getKeyName();
    private static final String TOKEN_VERSION_TYPE = SystemDataKeyType.TOKEN_VERSION.getKeyName();

    private static final String ADM_LNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.ADM_LNB_AUTH_MENU_LIST_NAME.getValueKey();

    private static final String LNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.LNB_AUTH_MENU_LIST_NAME.getValueKey();
    private static final String GNB_AUTH_MENU_LIST_NAME = SystemDataValueKey.GNB_AUTH_MENU_LIST_NAME.getValueKey();

    @Autowired
    private RedisOperations redisOperations;

    /**
     * Redis에 저장된 Access Token Secret Key 정보를 random한 값으로 새로 갱신하기.
     */
    public void setNewSecretKey() {
        String randomSecretKey = UUID.randomUUID().toString();

        LOGGER.debug("make new SecretKey : " + randomSecretKey);

        redisOperations.setData(BucketType.SYSTEM_DATA.getBucketId(), SECRET_KEY_TYPE, randomSecretKey);
    }

    /**
     * Redis에 저장된 Access Token의 Version 정보를 증가 시키기.
     */
    public void increaseTokenVersion() {
        String orgVersion = redisOperations.getData(BucketType.SYSTEM_DATA.getBucketId(), TOKEN_VERSION_TYPE);

        int newVersion = Integer.parseInt(orgVersion);

        redisOperations.setData(BucketType.SYSTEM_DATA.getBucketId(), TOKEN_VERSION_TYPE, String.valueOf((newVersion++)));
    }

    /**
     * Redis에 저장된 Access Token Secret Key 정보 반환.
     * 
     * @return String
     */
    public String getSecretKey() {
        return redisOperations.getData(BucketType.SYSTEM_DATA.getBucketId(), SECRET_KEY_TYPE);
    }

    /**
     * Redis에 저장된 Access Token Version 정보 반환.
     * 
     * @return String
     */
    public String getTokenVersion() {
        return "v" + redisOperations.getData(BucketType.SYSTEM_DATA.getBucketId(), TOKEN_VERSION_TYPE);
    }

    /**
     * 접근 권한에 Mapping 시킬 MenuId List를 저장.
     * 
     * @param authId
     *            String
     * @param lnbMenuList
     *            List<AuthorizedMenuDataVO>
     * @throws Exception
     */
    public void setAuthMenuList(JSONObject menuList, String menuType) throws Exception {
        String setKeyName = "";

        if(ADM_LNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            setKeyName = "ADM_" + SystemDataKeyType.LNB_MENU_AUTH.getKeyName();
        } else if(LNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            setKeyName = SystemDataKeyType.LNB_MENU_AUTH.getKeyName();
        } else if(GNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            setKeyName = SystemDataKeyType.GNB_MENU_AUTH.getKeyName();
        }

        LOGGER.info("Set Menu List To Redis : " + menuList);

        redisOperations.setData(BucketType.SYSTEM_DATA.getBucketId(), setKeyName, menuList.toString());
    }

    /**
     * 접근 권한에 Mapping된 Menu List Data를 삭제.
     * 
     * @param authId
     *            String
     * @throws Exception
     */
    public void removeAuthMenu(String authId) throws Exception {
        redisOperations.deleteData(BucketType.SYSTEM_DATA.getBucketId(), SystemDataKeyType.LNB_MENU_AUTH.getKeyName(authId));
    }

    /**
     * 접근 권한에 Mapping된 Menu List Data를 반환.
     * 
     * @param authIds
     *            String[]
     * @param menuType
     *            String
     * @param isSystemTenant
     *            boolean
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject getAuthMenuList(String[] authIds, String menuType, boolean isSystemTenant) throws Exception {
        String authMenuDataKey = "";
        String listName = "";

        if(ADM_LNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            authMenuDataKey = "ADM_" + SystemDataKeyType.LNB_MENU_AUTH.getKeyName();
            listName = LNB_AUTH_MENU_LIST_NAME;
        } else if(LNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            authMenuDataKey = SystemDataKeyType.LNB_MENU_AUTH.getKeyName();
            listName = LNB_AUTH_MENU_LIST_NAME;
        } else if(GNB_AUTH_MENU_LIST_NAME.equals(menuType)) {
            authMenuDataKey = SystemDataKeyType.GNB_MENU_AUTH.getKeyName();
            listName = GNB_AUTH_MENU_LIST_NAME;
        }

        String authMenuStr = redisOperations.getData(BucketType.SYSTEM_DATA.getBucketId(), authMenuDataKey);
        List<JSONObject> menuList = JSONConverter.jsonObjectToList(JSONObject.fromObject(authMenuStr), listName, JSONObject.class);

        return JSONConverter.listToJsonObject(this.getReqUserAuthMenu(menuList, authIds, isSystemTenant), listName);
    }

    private List<JSONObject> getReqUserAuthMenu(List<JSONObject> menuList, String[] authIds, boolean isSystemTenant) throws Exception {
        List<JSONObject> result = new ArrayList<JSONObject>();
        String menuAuthIds = null;
        boolean isAllowAccessMenu = false;
        boolean hasChildNode = false;
        boolean systemTenantOnly = false;
        List<JSONObject> childMenuList = null;

        for(JSONObject jsonAuthMenu : menuList) {
            menuAuthIds = jsonAuthMenu.get("allowAuthIds").toString();
            systemTenantOnly = (boolean) jsonAuthMenu.get("systemTenantOnly");

            isAllowAccessMenu = this.checkAuthMenu(menuAuthIds, authIds, systemTenantOnly, isSystemTenant);

            if(isAllowAccessMenu) {
                hasChildNode = (boolean) jsonAuthMenu.get("hasChildNode");

                if(hasChildNode) {
                    childMenuList = JSONConverter.jsonObjectToList(jsonAuthMenu, "childMenu", JSONObject.class);

                    jsonAuthMenu.remove("childMenu");
                    jsonAuthMenu.put("childMenu", this.getReqUserAuthMenu(childMenuList, authIds, isSystemTenant));
                }

                result.add(jsonAuthMenu);
            }
        }

        return result;
    }

    private boolean checkAuthMenu(String menuAuthIds, String[] requestedAuthIds, boolean systemTenantOnly, boolean isSystemTenant) {
        boolean result = false;
        String[] authArr = menuAuthIds.replace(" ", "").split(",");

        if(systemTenantOnly) {
            if(!isSystemTenant) {
                return false;
            }
        }

        for(String authId : authArr) {
            if("ALL".equals(authId)) {
                result = true;

                break;
            }

            for(String reqAuthId : requestedAuthIds) {
                if(reqAuthId.equals(authId)) {
                    result = true;

                    break;
                }
            }
        }

        return result;
    }
}
