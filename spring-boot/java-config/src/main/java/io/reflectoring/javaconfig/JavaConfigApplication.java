package io.reflectoring.javaconfig;

import io.reflectoring.javaconfig.model.Employee;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class JavaConfigApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfigAppConfiguration.class);
        // Now you can access beans from the context
        final Employee newEmployee = context.getBean("newEmployee", Employee.class);
        // ...
        context.close(); // Close the context when the application is shutting down
        SpringApplication.run(JavaConfigApplication.class, args);
    }
}
