package io.reflectoring.profiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class HelloBean {

  private static final Logger logger = LoggerFactory.getLogger(HelloBean.class);

  HelloBean(@Value("${helloMessage}") String helloMessage) {
    logger.info(helloMessage);
  }

}
