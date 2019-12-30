package io.reflectoring.profiles;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("foo")
@ConditionalOnProperty(value = "mock.enabled", havingValue = "false", matchIfMissing = true)
class FooBean {

  private static final Logger logger = LoggerFactory.getLogger(FooBean.class);

  @PostConstruct
  void postConstruct(){
    logger.info("loaded FooBean!");
  }

}
