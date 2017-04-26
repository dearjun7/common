package com.bh.subject.std.common.service.cache.valuekey;

public enum TenantCacheValueKey {
    COMMON_CODE_LIST_NAME("commonCodeList"),
    GLOBAL_OPTION_LIST_NAME("globalOptionList");

    private String valueKey;

    TenantCacheValueKey(String param) {
        this.valueKey = param;
    }

    public String getValueKey() {
        return this.valueKey;
    }
}
