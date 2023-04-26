package com.reflectoring.userdetails.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class CommonBean {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        //mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        //mapper.getSerializationConfig().without(MapperFeature.DEFAULT_VIEW_INCLUSION);
        //mapper.getDeserializationConfig().without(MapperFeature.DEFAULT_VIEW_INCLUSION);
        return mapper;
    }
}
