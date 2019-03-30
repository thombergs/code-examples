package io.reflectoring.configuration.mail;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.unit.DataSize;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
		"myapp.mail.enabled=asd",
		"myapp.mail.defaultSubject=hello",
		"myapp.mail.pauseBetweenMails=5s",
		"myapp.mail.maxAttachmentSize=1MB",
		"myapp.mail.smtpServers[0]=server1",
		"myapp.mail.smtpServers[1]=server2",
		"myapp.mail.maxAttachmentWeight=5kg"
})
class MailModuleTestWithAllProperties {

	@Autowired(required = false)
	private MailModuleProperties mailModuleProperties;

	@Test
	void propertiesAreLoaded() {
		assertThat(mailModuleProperties).isNotNull();
		assertThat(mailModuleProperties.getDefaultSubject()).isEqualTo("hello");
		assertThat(mailModuleProperties.getEnabled()).isTrue();
		assertThat(mailModuleProperties.getPauseBetweenMails()).isEqualByComparingTo(Duration.ofSeconds(5));
		assertThat(mailModuleProperties.getMaxAttachmentSize()).isEqualByComparingTo(DataSize.ofMegabytes(1));
		assertThat(mailModuleProperties.getSmtpServers()).hasSize(2);
		assertThat(mailModuleProperties.getMaxAttachmentWeight().getGrams()).isEqualTo(5000L);
	}
}