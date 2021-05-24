package io.pratik.springcloudrds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringcloudrdsApplicationTests {
	
	@Autowired
	private SystemRepository systemRepository;

	@Test
	void testCurrentDate() {
		String currentDate = systemRepository.getCurrentDate();
		System.out.println("currentDate "+currentDate);
	}

}
