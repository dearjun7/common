package com.bh.subject.std.common.service.cache.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Redis OperationVO
 * 
 * @author BH Jun
 */
public class OperationVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3272794274550644325L;

    private String bucketName = null;
    private DataVO dataVo = null;
    private List<DataVO> dataVoList = null;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public DataVO getDataVo() {
        return dataVo;
    }

    public void setDataVo(DataVO dataVo) {
        this.dataVo = dataVo;
    }

    public List<DataVO> getDataVoList() {
        return dataVoList;
    }

    public void setDataVoList(List<DataVO> dataVoList) {
        this.dataVoList = dataVoList;
    }
}
