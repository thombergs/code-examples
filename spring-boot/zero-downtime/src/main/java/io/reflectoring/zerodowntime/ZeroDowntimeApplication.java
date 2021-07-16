package io.reflectoring.zerodowntime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJdbcRepositories
@EnableWebMvc
public class ZeroDowntimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeroDowntimeApplication.class, args);
	}

}
