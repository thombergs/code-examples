/**
 * 
 */
package io.pratik.springses;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import io.awspring.cloud.ses.SimpleEmailServiceMailSender;

/**
 * @author pratikdas
 *
 */
@Configuration
public class MailConfig {
	  @Bean
	  public AmazonSimpleEmailService amazonSimpleEmailService() {

	    return AmazonSimpleEmailServiceClientBuilder.standard()
	        .withCredentials(new ProfileCredentialsProvider("pratikpoc"))
	        .withRegion(Regions.US_EAST_1)
	        .build();
	  }

	  @Bean
	  public JavaMailSender javaMailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
	    return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
	  }
	  
	  @Bean
	  public MailSender mailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
	    return new SimpleEmailServiceMailSender(amazonSimpleEmailService);
	  }
}
