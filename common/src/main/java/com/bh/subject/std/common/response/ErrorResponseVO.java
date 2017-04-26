package com.bh.subject.std.common.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * ErrorResponseVO
 * 
 * @author BH Jun
 */

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ErrorMessageVO.class})
public class ErrorResponseVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1618057969899763515L;

    private ResponseHeaderVO header = new ResponseHeaderVO();
    private ErrorResponseBodyVO body = new ErrorResponseBodyVO();

    public ResponseHeaderVO getHeader() {
        return header;
    }

    public void setHeader(ResponseHeaderVO header) {
        this.header = header;
    }

    public ErrorResponseBodyVO getBody() {
        return body;
    }

    public void setBody(ErrorResponseBodyVO body) {
        this.body = body;
    }
}
