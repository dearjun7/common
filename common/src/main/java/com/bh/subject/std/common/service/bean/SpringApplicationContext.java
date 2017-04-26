package com.bh.subject.std.common.service.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring Bean 객체를 Java POJO 객체에서 바인딩할 수 있도록 지원하는 Class.
 * 
 * @author BH Jun
 */
public class SpringApplicationContext implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationContext.class);
    private static ApplicationContext CONTEXT;

    public SpringApplicationContext() {
        LOGGER.info("init SpringApplicationContext");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }

    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        return CONTEXT.getBean(beanName, requiredType);
    }
}