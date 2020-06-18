package io.reflectoring.cache.configiration;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@EnableCaching
@Profile("client")
public class ClientCacheConfig {

    @Bean
    ClientConfig config() {
        ClientConfig clientConfig = new ClientConfig();

        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(300);
        return clientConfig;
    }

    @Bean
    HazelcastInstance hazelcastInstance() {
        return HazelcastClient.newHazelcastClient();
    }

    @Bean
    CacheManager cacheManager() {
        return new HazelcastCacheManager(hazelcastInstance());
    }

}
