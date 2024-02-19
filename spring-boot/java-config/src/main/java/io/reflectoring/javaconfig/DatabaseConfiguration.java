package io.reflectoring.javaconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "io.reflectoring.javaconfig")
@PropertySource("classpath:application.properties")
public class DatabaseConfiguration {
    @Value("${spring.datasource.driver-class-name}")
    private String databaseClass;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "employee-management-db")
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseClass);
        dataSource.setUrl(databaseUrl); // H2 in-memory database URL
        dataSource.setUsername(username); // Default H2 username (sa)
        dataSource.setPassword(password); // Default H2 password (sa)

        return dataSource;
    }

}
