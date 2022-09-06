package com.reflectoring.library;

import com.reflectoring.library.model.persistence.Book;
import com.reflectoring.library.repository.BookRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {

        return (args) -> {
            // save a few books
            repository.saveAll(Arrays.asList(new Book("ABC", "Bauer", "2008",
                                    "Keely Steel")))
                    .blockLast(Duration.ofSeconds(10));

            // fetch all books
            log.info("Books found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(book -> {
                log.info(book.toString());
            }).blockLast(Duration.ofSeconds(10));
        };
    }


}
