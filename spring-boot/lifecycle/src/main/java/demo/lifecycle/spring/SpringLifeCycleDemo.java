package demo.lifecycle.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringLifeCycleDemo {
    public static void main(String argv[]) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringLifeCycleDemo.class, argv);
        List<String> movieList = Arrays.asList("Superman", "Batman", "Spiderman");
        MovieRental movieRental = applicationContext.getBean(MovieRental.class);
        movieRental.movieCheckout(movieList);
        ((BeanDefinitionRegistry) applicationContext).removeBeanDefinition("movieRental");
    }
}

