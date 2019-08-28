package io.reflectoring.starter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarterApplicationTests {

	@Autowired(required = false)
	private EventPublisher eventPublisher;

	@Test
	public void eventPublisherIsAvailable() {
		assertThat(eventPublisher).isNotNull();
	}

}
