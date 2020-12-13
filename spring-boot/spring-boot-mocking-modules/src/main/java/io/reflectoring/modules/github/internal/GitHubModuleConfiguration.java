package io.reflectoring.modules.github.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GitHubModuleConfiguration {

    @Bean
    GitHubService gitHubService(){
        return new GitHubService();
    }

}
