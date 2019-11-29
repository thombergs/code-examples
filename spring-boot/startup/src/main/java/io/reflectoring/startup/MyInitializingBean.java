package io.reflectoring.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
class MyInitializingBean implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MyInitializingBean.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("InitializingBean#afterPropertiesSet()");
    }

}
