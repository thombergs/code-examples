package io.reflectoring.client.registration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer();

        private static void startContainers() {
            Startables.deepStart(Stream.of(rabbitMQContainer)).join();
            // we can add further containers here like rabbitmq or other database
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.rabbitmq.host", rabbitMQContainer.getHost(),
                    "spring.rabbitmq.port", rabbitMQContainer.getAmqpPort().toString(),
                    "spring.rabbitmq.username", rabbitMQContainer.getAdminUsername(),
                    "spring.rabbitmq.password", rabbitMQContainer.getAdminPassword()
            );
        }


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration()
            );
            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
