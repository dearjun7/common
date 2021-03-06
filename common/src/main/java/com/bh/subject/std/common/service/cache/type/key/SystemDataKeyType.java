package com.bh.subject.std.common.service.cache.type.key;

/**
 * GMS API Service에서 모든 Tenant들이 전역적으로 사용하는 Redis Data에 대한 Key Type Class.
 * 
 * @author BH Jun
 */
public enum SystemDataKeyType {
    TOKEN_SECRET_KEY {

        @Override
        public String getKeyName() {
            return this.toString();
        }

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }
    },
    TOKEN_VERSION {

        @Override
        public String getKeyName() {
            return this.toString();
        }

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }
    },
    LNB_MENU_AUTH {

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getKeyName() {
            return this.toString();
        }
    },
    GNB_MENU_AUTH {

        @Override
        public String getKeyName(String param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getKeyName() {
            return this.toString();
        }
    };

    /**
     * SystemDataKeyType의 Key Name을 반환.
     * 
     * @return String
     */
    public abstract String getKeyName();

    /**
     * SystemDataKeyType의 Key Name을 반환.
     * 
     * @param menuId
     *            String
     * @return String
     */
    public abstract String getKeyName(String menuId);
}
