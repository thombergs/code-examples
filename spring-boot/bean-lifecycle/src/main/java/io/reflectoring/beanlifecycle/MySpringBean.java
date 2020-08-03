package io.reflectoring.beanlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class MySpringBean implements BeanNameAware, ApplicationContextAware,
        InitializingBean, DisposableBean {

    private String message;

    public void sendMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("--- setBeanName executed ---");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        System.out.println("--- setApplicationContext executed ---");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("--- @PostConstruct executed ---");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("--- afterPropertiesSet executed ---");
    }

    public void initMethod() {
        System.out.println("--- init-method executed ---");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("--- @PreDestroy executed ---");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("--- destroy executed ---");
    }

    public void destroyMethod() {
        System.out.println("--- destroy-method executed ---");
    }

}