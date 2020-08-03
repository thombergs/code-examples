package io.reflectoring.beanlifecycle.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;

@Configuration
public class JerseyConfig extends ResourceConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerResources() {
        applicationContext.getBeansWithAnnotation(Path.class).values().forEach(this::register);
    }

}