package com.bh.subject.std.common.service.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * Spring 의 CookieLocaleResolver 클래스를 상속하여 Locale(언어) 정보를 Cookie 에
 * 설정하는 Class
 * 
 * @author BH Jun
 */
public class GMSCookieLocaleResolver extends CookieLocaleResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(GMSCookieLocaleResolver.class);

    /**
     * 콤마(,)로 구분되는 지원 locale language 들을 저장하는 language String (예: "en, ko")
     */
    private String supportedLocale;

    /**
     * 지원하지 않는 언어일 경우 return 되는 기본 언어 값 (예 : "ko")
     */
    private String defaultLocaleLang;

    /**
     * 생성자
     */
    public GMSCookieLocaleResolver() {
        super();
    }

    /**
     * setDefaultLocaleLang
     * 
     * @param defaultLocaleLang
     *            String 기본으로 설정할 Locale Language 값을 String으로 입력한다.
     */
    public void setDefaultLocaleLang(String defaultLocaleLang) {
        this.defaultLocaleLang = defaultLocaleLang;
    }

    /**
     * getDefaultLocaleLang
     * 
     * @return defaultLocaleLang String
     *         기본으로 설정된 Locale Language
     */
    public String getDefaultLocaleLang() {
        return this.defaultLocaleLang;
    }

    /**
     * Set supported locale language String
     * 
     * @param supportedLocale
     *            콤마(,)로 구분되는 Locale language 들의 String (예: "en, ko")
     */
    public void setSupportedLocale(String supportedLocale) {
        this.supportedLocale = supportedLocale;
    }

    /**
     * Get supported locale String
     * 
     * @return String 콤마(,)로 구분되는 Locale language 들의 String(예: "en, ko")
     */
    public String getSupportedLocale() {
        return this.supportedLocale;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.web.servlet.i18n.CookieLocaleResolver#setLocale(javax
     * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.util.Locale)
     */
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if(locale != null) {
            // 지원하는 언어인지 check 하고 지원하지 않으면 영어로 설정된 Locale 를 가져온다.
            locale = checkSupportedLocale(locale);
        }

        // parent method 호출하여 Cookie 에 Locale(언어 value) 설정
        LOGGER.debug("locale = " + locale);
        super.setLocale(request, response, locale);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.i18n.CookieLocaleResolver#
     * determineDefaultLocale(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {

        // parent method 먼저 호출하여 default Locale 먼저 설정
        Locale defaultLocale = super.determineDefaultLocale(request);

        // 지원하는 언어인지 check 하고 지원하지 않으면 영어로 설정된 Locale 를 가져온다.
        defaultLocale = checkSupportedLocale(defaultLocale); //

        return defaultLocale;
    }

    /**
     * 파라미터로 넘겨준 Locale 이 현재 지원하는 Locale 인지 확인하여 지원하는 언어의 Locale 이면 그대로 return
     * 하고 지원하지 않는 Locale 이면 servlet Context에서 설정한 defaultLocaleLang 값을 리턴한다.
     * 
     * @param locale
     *            지원하는 Locale 인지 체크할 Locale 변수
     * @return 파라미터로 넘겨준 Locale(지원하는 Locale 일 경우) 또는 servlet Context에서 설정한
     *         defaultLocaleLang 값을 리턴한다.
     */
    private Locale checkSupportedLocale(Locale locale) {
        LOGGER.debug("START check locale = " + locale);

        String language = locale.getLanguage();
        //        String country = locale.getCountry();
        // country 코드가 없거나 길이가 2바이트 이하이면 default Locale return
        //        if(country == null || "".equals(country) || country.length() < 2) {
        if(language == null || "".equals(language) || language.length() < 2) {
            return new Locale(this.defaultLocaleLang);
        }

        Locale supportedLocale = null;

        String[] supportLocaleString = this.supportedLocale.split(",");

        // 지원하는 locale 이면 그대로 return
        for(int i = 0; i < supportLocaleString.length; i++) {
            if(supportLocaleString[i].equals(language)) {
                supportedLocale = locale;
                break;
            }
        }

        // 지원하지 않는 locale이면 return
        if(supportedLocale == null) {
            supportedLocale = new Locale(this.defaultLocaleLang);
        }

        return supportedLocale;
    }

}