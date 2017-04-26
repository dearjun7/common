package com.bh.subject.std.common.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.bh.subject.std.common.pagination.PaginationVO;

@JsonPropertyOrder({"pagination", "docCnt", "docs"})
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsePaginationBodyVO<T>extends ResponseBodyVO<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 7519568808248152329L;

    private PaginationVO pagination;

    public PaginationVO getPagination() {
        return pagination;
    }

    public void setPagination(PaginationVO pagination) {
        this.pagination = pagination;
    }

    public void setBody(ResponseBodyVO<T> body) {
        super.setDocCnt(body.getDocCnt());
        super.setDocs(body.getDocs());
    }
}
