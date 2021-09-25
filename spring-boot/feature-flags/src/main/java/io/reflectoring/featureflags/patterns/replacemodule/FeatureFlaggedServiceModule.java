package io.reflectoring.featureflags.patterns.replacemodule;

import io.reflectoring.featureflags.FeatureFlagService;
import io.reflectoring.featureflags.patterns.replacebean.FeatureFlaggedBean;
import io.reflectoring.featureflags.patterns.replacemodule.newmodule.NewService1;
import io.reflectoring.featureflags.patterns.replacemodule.newmodule.NewService2;
import io.reflectoring.featureflags.patterns.replacemodule.oldmodule.OldService1;
import io.reflectoring.featureflags.patterns.replacemodule.oldmodule.OldService2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureFlaggedServiceModule {

    private final FeatureFlagService featureFlagService;

    public FeatureFlaggedServiceModule(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @Bean("replaceModuleService1")
    FeatureFlaggedBean<Service1> service1() {
        return new FeatureFlaggedBean<>(Service1.class, featureFlagService::isNewServiceEnabled, new NewService1(), new OldService1());
    }

    @Bean("replaceModuleService2")
    FeatureFlaggedBean<Service2> service2() {
        return new FeatureFlaggedBean<>(Service2.class, featureFlagService::isNewServiceEnabled, new NewService2(), new OldService2());
    }

}
