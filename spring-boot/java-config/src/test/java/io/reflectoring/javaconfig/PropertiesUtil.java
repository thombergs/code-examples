package io.reflectoring.javaconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Component
public class PropertiesUtil {
    public Properties loadApplicationProperties() {
        return loadProperties("application.properties");
    }

    public Properties loadProperties(String propFilepath) {
        Properties properties = null;
        try {
            properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader()
                                          .getResourceAsStream(propFilepath);
            properties.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load the file");
        }
        return properties;
    }
}
