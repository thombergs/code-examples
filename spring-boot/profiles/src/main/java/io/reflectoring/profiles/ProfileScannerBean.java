package io.reflectoring.profiles;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class ProfileScannerBean {

  private static final Logger logger = LoggerFactory.getLogger(ProfileScannerBean.class);

  private Environment environment;

  ProfileScannerBean(Environment environment) {
    this.environment = environment;
  }

  @PostConstruct
  void postConstruct(){
    String[] activeProfiles = environment.getActiveProfiles();
    logger.info("the following profiles are active: {}", Arrays.toString(activeProfiles));
  }

}
