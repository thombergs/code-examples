package io.reflectoring.cache.configiration;


import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@EnableCaching
@Profile("embedded")
public class EmbeddedCacheConfig /*extends CachingConfigurerSupport*/ {

    @Bean
    Config config(){
        Config config = new Config();

        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(300);
        //mapConfig.setMaxIdleSeconds(10);

        config.getMapConfigs().put("cars", mapConfig);
        return config;
    }

}
