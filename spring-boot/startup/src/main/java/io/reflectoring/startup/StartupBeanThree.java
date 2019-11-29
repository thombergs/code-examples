package io.reflectoring.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
//@DependsOn("startupBeanOne")
public class StartupBeanThree implements ApplicationListener<ApplicationReadyEvent>, ApplicationRunner, CommandLineRunner, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(StartupBeanThree.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("InitializingBean#afterPropertiesSet()");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("ApplicationRunner#run()");
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("CommandLineRunner#run(String[])");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("ApplicationListener#onApplicationEvent(ApplicationReadyEvent)");
    }

    @PostConstruct
    @Order(1) // not evaluated by Spring
    void postConstruct() {
        logger.info("@PostConstruct");
    }
}
