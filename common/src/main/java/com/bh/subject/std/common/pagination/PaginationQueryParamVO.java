package com.bh.subject.std.common.pagination;

import java.io.Serializable;

/**
 * PaginationQueryParamVO
 * 
 * @author BH Jun
 * @param <T>
 */
public class PaginationQueryParamVO<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2226053898407012961L;

    private T queryParam;
    private PaginationVO paginationVO;
    private String systemTenantId;
    private String language;

    public T getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(T queryParam) {
        this.queryParam = queryParam;
    }

    public PaginationVO getPaginationVO() {
        return paginationVO;
    }

    public void setPaginationVO(PaginationVO paginationVO) {
        this.paginationVO = paginationVO;
    }

    public String getSystemTenantId() {
        return systemTenantId;
    }

    public void setSystemTenantId(String systemTenantId) {
        this.systemTenantId = systemTenantId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
