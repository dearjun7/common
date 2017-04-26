package com.bh.subject.std.common.datasource;

import com.bh.subject.std.common.datasource.type.DataSourceType;

/**
 * User의 요청 thread 별로 User Tenant의 계약 Datasoruce Type을 저장, 선택하는 class.
 * 
 * @author BH Jun
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

    public static void setDatasourceType(DataSourceType datasourceType) {
        contextHolder.set(datasourceType);
    }

    public static DataSourceType getDataSourceType() {
        if(contextHolder == null) {
            contextHolder.set(DataSourceType.MARIADB);
        }
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
