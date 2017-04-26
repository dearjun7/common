package com.bh.subject.std.common.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * ResponseMap
 * 
 * @author BH Jun
 */

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonTypeName("response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseVO<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5439262302605191671L;

    private ResponseHeaderVO header = new ResponseHeaderVO();
    private ResponseBodyVO<T> body = new ResponseBodyVO<T>();

    public ResponseHeaderVO getHeader() {
        return header;
    }

    public void setHeader(ResponseHeaderVO header) {
        this.header = header;
    }

    public ResponseBodyVO<T> getBody() {
        return body;
    }

    public void setBody(ResponseBodyVO<T> body) {
        this.body = body;
    }
}
