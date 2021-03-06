package com.bh.subject.std.common.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import com.bh.subject.std.common.pagination.PaginationVO;
import com.bh.subject.std.common.response.CommonResultDataVO;
import com.bh.subject.std.common.response.ResponsePaginationBodyVO;
import com.bh.subject.std.common.response.ResponseVO;

/**
 * GMSCommonController
 * 
 * @author BH Jun
 */
public abstract class GMSCommonController {

    @Autowired
    @Qualifier("messageSource")
    MessageSource messageSource;
    @Value("#{config['gms.cookie.locale.name']}")
    private String localeStr;
    @Value("#{config['gms.page.default.size']}")
    private String defaultPageViewSize;

    /**
     * getProcessSuccessCode
     * 
     * @return String 성공 코드를 반환한다.
     */
    protected CommonResultDataVO getProcessSuccessCode() {
        String resultStr = messageSource.getMessage("SCD", null, new Locale("en"));
        CommonResultDataVO result = new CommonResultDataVO();
        result.setResult(resultStr);

        return result;
    }

    /**
     * getProcessFailCode
     * 
     * @return String 실패 코드를 반환한다.
     */
    protected CommonResultDataVO getProcessFailCode() {
        String resultStr = messageSource.getMessage("FLR", null, new Locale("en"));
        CommonResultDataVO result = new CommonResultDataVO();
        result.setResult(resultStr);

        return result;
    }

    /**
     * List 타입의 데이터로 구성된 responseData를 생성한다.
     * 
     * @param status
     *            HttpStatus response 상태 코드
     * @param resultDataList
     *            List<T> response할 List 타입의 data
     * @return ResponseVO<T>
     */
    protected <T> ResponseVO<T> makeResponseData(HttpStatus status, List<T> resultDataList) {
        ResponseVO<T> response = new ResponseVO<T>();

        response.getHeader().setStatus(status.value());
        response.getBody().setDocCnt(resultDataList.size());
        response.getBody().setDocs(resultDataList);

        return response;
    }

    /**
     * 단일 오브젝트 타입의 데이터로 구성된 responseData를 생성한다.
     * 
     * @param status
     *            HttpStatus response 상태 코드
     * @param resultData
     *            T response할 오브젝트 타입의 data
     * @return ResponseVO<T>
     */
    protected <T> ResponseVO<T> makeResponseData(HttpStatus status, T resultData) {
        ResponseVO<T> response = new ResponseVO<T>();

        response.getHeader().setStatus(status.value());
        response.getBody().setDocCnt(1);
        response.getBody().setDoc(resultData);

        return response;
    }

    protected <T> ResponseVO<T> makeResponseData(HttpStatus status, List<T> resultDataList, int currentPageNum, int pageViewSize,
            int totalCount) {
        ResponseVO<T> response = this.makeResponseData(status, resultDataList);
        ResponsePaginationBodyVO<T> body = new ResponsePaginationBodyVO<T>();

        PaginationVO pagination = new PaginationVO();

        pagination.setTotalCount(totalCount);
        pagination.setPageViewSize(pageViewSize == 0 ? Integer.parseInt(defaultPageViewSize) : pageViewSize);
        pagination.setCurrentPageNum(currentPageNum);

        body.setPagination(pagination);
        body.setBody(response.getBody());

        response.setBody(body);

        return response;
    }

    /**
     * 리턴 데이터 형태가 XML일 경우(Accept:application/xml) 사용하는 method로,
     * response 객체를 responseWrapper로 Wrapping하여 리턴한다.
     * ResponseVO responseWrapper 파라메터는 해당 method를 선언하기 전에 반드시 원본 ResponseVO의
     * Type을 Wrapping하는 Wrapper를 생성한 후에
     * 이를 파라메터로 선언해야한다.
     * 
     * @param response
     *            ResponseVO<T> 원본 response 객체
     * @param responseWrapper
     *            ResponseVO<T> XML marshalling할 원본 response의 type을 wrapping한 객체
     * @return responseWrapper ResponseVO<T>
     */
    protected <T> ResponseVO<T> wrappingResponseDataForXML(ResponseVO<T> response, ResponseVO<T> responseWrapper) {
        responseWrapper.getHeader().setStatus(response.getHeader().getStatus());
        responseWrapper.getBody().setDocCnt(response.getBody().getDocCnt());
        responseWrapper.getBody().setDocs((List<T>) response.getBody().getDocs());

        return responseWrapper;
    }

    protected <T> ResponseVO<T> wrappingResponseDataForXML(ResponseVO<T> response, ResponseVO<T> responseWrapper, int currentPageNum,
            int pageViewSize, int totalCount) {
        ResponseVO<T> result = this.wrappingResponseDataForXML(response, responseWrapper);
        ResponsePaginationBodyVO<T> body = new ResponsePaginationBodyVO<T>();
        PaginationVO pagination = new PaginationVO();

        pagination.setTotalCount(totalCount);
        pagination.setPageViewSize(pageViewSize == 0 ? Integer.parseInt(defaultPageViewSize) : pageViewSize);
        pagination.setCurrentPageNum(currentPageNum);

        body.setPagination(pagination);
        body.setBody(result.getBody());

        result.setBody(body);

        return result;
    }
}
