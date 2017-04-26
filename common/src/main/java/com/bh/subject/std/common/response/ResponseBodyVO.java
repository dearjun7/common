package com.bh.subject.std.common.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseBodyVO<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -47775222615253443L;

    @XmlAttribute(name = "docCnt")
    private int docCnt = 0;

    @XmlElement(name = "doc")
    private List<T> docs = new ArrayList<T>();

    public int getDocCnt() {
        return docCnt;
    }

    public void setDocCnt(int docCnt) {
        this.docCnt = docCnt;
    }

    public List<T> getDocs() {
        return docs;
    }

    public void setDocs(List<T> docs) {
        this.docs = docs;
    }

    public void setDoc(T doc) {
        this.docs.add(doc);
    }
}
