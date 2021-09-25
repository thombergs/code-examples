package io.reflectoring.featureflags.patterns.replacemodule;

import io.reflectoring.featureflags.patterns.replacemodule.oldmodule.OldService1;
import io.reflectoring.featureflags.patterns.replacemodule.oldmodule.OldService2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ServiceModule {

    @Bean
    Service1 service1() {
        return new OldService1();
    }

    @Bean
    Service2 service2() {
        return new OldService2();
    }

}
