package io.reflectoring.beanlifecycle.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class NamedSpringBean implements BeanNameAware {

    Logger logger = LoggerFactory.getLogger(NamedSpringBean.class);

    public void setBeanName(String name) {
        logger.info(name + " created.");
    }

}