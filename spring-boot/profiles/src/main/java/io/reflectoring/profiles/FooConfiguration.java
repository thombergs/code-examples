package io.reflectoring.profiles;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("foo")
public class FooConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(FooConfiguration.class);

  @PostConstruct
  void postConstruct(){
    logger.info("loaded FooConfiguration!");
  }

}
