package com.bh.subject.std.common.service.cache.type.key;

/**
 * GMS API Service에서 Tenant 단위로 사용하는 Redis Data에 대한 Key Type Class.
 * 
 * @author BH Jun
 */
public enum TenantCacheKeyType {
    DATASOURCE {

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getKeyName() {
            return this.toString();
        }
    },
    COMMON_CODE {

        @Override
        public String getKeyName(String upperCode) {
            return this.toString() + ":" + upperCode;
        }

        @Override
        public String getKeyName() {
            throw new UnsupportedOperationException();
        }
    },
    GLOBAL_OPTION {

        @Override
        public String getKeyName() {
            return this.toString();
        }

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * TenantCacheKeyType의 Key Name을 반환.
     * 
     * @return String
     */
    public abstract String getKeyName();

    /**
     * TenantCacheKeyType의 Key Name을 반환.
     * 
     * @param authId
     *            String
     * @return String
     */
    public abstract String getKeyName(String param);
}
