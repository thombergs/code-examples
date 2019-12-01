package io.reflectoring.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@DependsOn("myApplicationListener")
class MyPostConstructBean {

    private static final Logger logger = LoggerFactory.getLogger(MyPostConstructBean.class);

    @PostConstruct
    @Order(2) // not evaluated by Spring
    void postConstruct(){
        logger.info("@PostConstruct");
    }

}
