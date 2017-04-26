package com.bh.subject.std.common.dao;

import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import com.bh.subject.std.common.datasource.DataSourceContextHolder;
import com.bh.subject.std.common.datasource.type.DataSourceType;

public class GMSMaxValueIncrementer extends AbstractDataFieldMaxValueIncrementer {

    private AbstractDataFieldMaxValueIncrementer incrementer;
    private String columnName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    protected long getNextKey() {
        return this.getInstance().nextLongValue();
    }

    protected String getNextKeyString() {
        return this.getInstance().nextStringValue();
    }

    private AbstractDataFieldMaxValueIncrementer getInstance() {
        DataSourceType dataSourceType = DataSourceContextHolder.getDataSourceType();

        if(dataSourceType == null) {
            dataSourceType = DataSourceType.MARIADB;
        }

        if(incrementer == null) {
            if(dataSourceType == DataSourceType.ORACLE) {
                incrementer = new OracleSequenceMaxValueIncrementer(getDataSource(), getIncrementerName());
            } else if(dataSourceType == DataSourceType.MARIADB) {
                incrementer = new MySQLMaxValueIncrementer(getDataSource(), getIncrementerName(), getColumnName());
            }
            incrementer.setPaddingLength(20);
        }
        return incrementer;
    }
}
