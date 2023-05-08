package io.pratik;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountInquiryApplication {
	private static final Logger LOG = LogManager.getLogger(AccountInquiryApplication.class);

	public static void main(String[] args) {
		LOG.info("Starting application");

		SpringApplication.run(AccountInquiryApplication.class, args);

	}

}
