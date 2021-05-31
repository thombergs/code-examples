package io.reflectoring.springcloudredis.configuration;

import io.lettuce.core.ReadFrom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class EnableCache {
    public static final int PORT = 6379;

    @Value("${spring.redis.primary}")
    private String primaryEndpoint;

    @Value("${spring.redis.reader}")
    private String readerEndpoint;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        var staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(this.primaryEndpoint, PORT);
        staticMasterReplicaConfiguration.addNode(readerEndpoint, PORT);
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

}
