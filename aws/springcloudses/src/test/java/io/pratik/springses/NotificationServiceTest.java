/**
 * 
 */
package io.pratik.springses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author pratikdas
 *
 */
@SpringBootTest
class NotificationServiceTest {
	
	@Autowired
	private NotificationService notificationService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSendMail() {
	    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("pratikd2000@gmail.com");
        simpleMailMessage.setTo("pratikd2027@gmail.com");
        simpleMailMessage.setSubject("test subject");
        simpleMailMessage.setText("test text");
        
		notificationService.sendMailMessage(simpleMailMessage);
	}
	
	@Test
	void testSendMailWithAttachments() {
	    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("pratikd2000@gmail.com");
        simpleMailMessage.setTo("pratikd2027@gmail.com");
        simpleMailMessage.setSubject("test subject");
        simpleMailMessage.setText("test text");
        
		notificationService.sendMailMessage(simpleMailMessage);
	}

}
