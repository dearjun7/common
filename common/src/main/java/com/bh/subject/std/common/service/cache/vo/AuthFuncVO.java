/**
 * Any software product designated as "Handysoft Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of Handysoft CO., LTD (“Handysoft”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for Handysoft’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** Handysoft does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 6. 20. First Draft
 */
package com.bh.subject.std.common.service.cache.vo;

import java.io.Serializable;
import java.util.List;

/**
 * AuthFuncVO.java
 * 
 * @author BH Jun
 */
public class AuthFuncVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 968475085864962853L;

    private String bucketId;
    private String authKey;
    private List<String> functionURIList;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public List<String> getFunctionURIList() {
        return functionURIList;
    }

    public void setFunctionURIList(List<String> functionURIList) {
        this.functionURIList = functionURIList;
    }
}
