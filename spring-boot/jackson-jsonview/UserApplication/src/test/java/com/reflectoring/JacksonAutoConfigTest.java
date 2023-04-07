package com.reflectoring;

import ch.qos.logback.core.Context;
import com.fasterxml.jackson.databind.*;
import org.apache.catalina.core.ApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JacksonAutoConfigTest {

    private AnnotationConfigApplicationContext context;
    @Before
    public void setUp() {
        this.context = new AnnotationConfigApplicationContext();
    }

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void defaultObjectMapperBuilder() throws Exception {
        this.context.register(JacksonAutoConfiguration.class);
        this.context.refresh();
        Jackson2ObjectMapperBuilder builder=this.context.getBean(Jackson2ObjectMapperBuilder.class);
        ObjectMapper mapper=builder.build();
        assertTrue(MapperFeature.DEFAULT_VIEW_INCLUSION.enabledByDefault());
        assertFalse(mapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
        assertTrue(MapperFeature.DEFAULT_VIEW_INCLUSION.enabledByDefault());
        assertFalse(mapper.getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
        assertFalse(mapper.getSerializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));

        SerializationConfig serConfig = mapper.getSerializationConfig();
        assertFalse(serConfig.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
        serConfig = serConfig.with(MapperFeature.DEFAULT_VIEW_INCLUSION);
        assertTrue(serConfig.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
    }
}
