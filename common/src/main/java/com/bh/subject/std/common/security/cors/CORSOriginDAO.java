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
 * BH Jun 2016. 10. 14. First Draft
 */
package com.bh.subject.std.common.security.cors;

import org.springframework.stereotype.Repository;

import com.bh.subject.std.common.dao.GMSCommonDAO;

/**
 * CORSOriginDAO.java
 * 
 * @author BH Jun
 */
@Repository
public class CORSOriginDAO extends GMSCommonDAO {

    public boolean selectIsCORSOrigin(String reqOriginURL) {
        return ((int) super.getSqlSession().selectOne("cors.selectIsCORSOrigin", reqOriginURL)) > 0 ? true : false;
    }
}
