package com.bh.subject.std.common.service.cache.vo;

import java.io.Serializable;

/**
 * Redis DataVO
 * 
 * @author BH Jun
 */
public class DataVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8663735619654167991L;

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
