package io.reflectoring.pagination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;

@SpringBootApplication
public class PaginationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaginationApplication.class, args);
	}

}
