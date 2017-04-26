package com.bh.subject.std.common.test;

import java.security.MessageDigest;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.bh.subject.std.common.datasource.DataSourceContextHolder;
import com.bh.subject.std.common.datasource.type.DataSourceType;

@RunWith(GMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/resources/config/spring/context-*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractApplicationContext {

    @Value("#{config['gms.cookie.token.name']}")
    private String accessTokenName;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected static final String SERVER_ENV = "local";

    protected final String testEmail = "junitTest@handysoft.co.kr";
    protected String testPw = null;
    protected final String stringTestPw = "handy~soft21";
    protected final String testDeviceType = "WEB";
    protected final String testClientIP = "127.0.0.1";
    protected String loginParam = null;

    protected final String testTenantId = "001000000";
    protected final String testUserId = "001000002";
    protected final String testUserName = "JunitTest";
    protected final String testDeptId = "000000101";

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @PostConstruct
    public void init() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte hash[] = digest.digest(stringTestPw.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) sb.append('0');
                sb.append(hex);
            }

            testPw = sb.toString();
            loginParam = "{\"email\":\"" + testEmail + "\", \"accountPw\":\"" + testPw + "\"}";
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void Test() {
        if(System.getProperty("env") == null) {
            System.setProperty("env", SERVER_ENV);
        }

        setDatasourceType();
    }

    protected Cookie getAccessToken(MockMvc mockMvc) throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.post("/account/login").contentType(MediaType.APPLICATION_JSON).content(loginParam));
        return resultActions.andReturn().getResponse().getCookie(accessTokenName);
    }

    private static void setDatasourceType() {
        DataSourceContextHolder.setDatasourceType(DataSourceType.MARIADB);
    }
}
