package io.reflectoring.profiles;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
class DefaultBean {

  private static final Logger logger = LoggerFactory.getLogger(DefaultBean.class);

  @PostConstruct
  void postConstruct(){
    logger.info("loaded DefaultBean!");
  }

}
