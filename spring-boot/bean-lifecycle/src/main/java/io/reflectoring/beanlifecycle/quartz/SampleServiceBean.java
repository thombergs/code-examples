package io.reflectoring.beanlifecycle.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceBean {

    Logger logger = LoggerFactory.getLogger(SampleServiceBean.class);

    public void hello() {
        logger.info("hello from Quartz Job!");
    }

}
