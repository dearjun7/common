package com.bh.subject.std.common.service.cache.valuekey;

public enum SystemDataValueKey {
    ADM_LNB_AUTH_MENU_LIST_NAME("admLnbMenuList"),
    LNB_AUTH_MENU_LIST_NAME("lnbMenuList"),
    GNB_AUTH_MENU_LIST_NAME("gnbMenuList"),
    MENU_MEMBER_VAL_NAME("menuId");

    private String valueKey;

    SystemDataValueKey(String param) {
        this.valueKey = param;
    }

    public String getValueKey() {
        return this.valueKey;
    }
}