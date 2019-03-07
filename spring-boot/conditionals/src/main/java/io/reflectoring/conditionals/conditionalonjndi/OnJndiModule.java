package io.reflectoring.conditionals.conditionalonjndi;

import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnJndi("java:comp/env/foo")
class OnJndiModule {
}
