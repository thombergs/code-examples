package io.reflectoring.featureflags.ff4j;

import org.ff4j.FF4j;
import org.ff4j.conf.XmlParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FF4JConfiguration {

    @Bean("ff4jConfig")
    public FF4j ff4J(){
        return new FF4j(new XmlParser(), "ff4j.xml");
    }
}
