package io.reflectoring.componentscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.reflectoring.birds.BirdsExplicitScan;

@SpringBootApplication
@Import(value= {BirdsExplicitScan.class})
public class SpringComponentScanningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringComponentScanningApplication.class, args);
	}

}
