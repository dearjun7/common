package com.bh.subject.std.common.response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * CommonResponseWrapperVO
 * 
 * @author BH Jun
 */
@XmlRootElement(name = "response")
@XmlSeeAlso({CommonResultDataVO.class})
public class CommonResponseWrapperVO extends ResponseVO<CommonResultDataVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 1698026766808176571L;

}
