package io.reflectoring.zerodowntime;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class LaunchDarklyConfiguration {

    private LDClient launchdarklyClient;

    @Bean
    public LDClient launchdarklyClient(@Value("${launchdarkly.sdkKey}") String sdkKey) {
        this.launchdarklyClient = new LDClient(sdkKey);
        return this.launchdarklyClient;
    }

    @PreDestroy
    public void destroy() throws IOException {
        this.launchdarklyClient.close();
    }

}