package io.reflectoring.beanlifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BeanLifecycleApplicationTests {

    @Autowired
    public MySpringBean mySpringBean;

    @Test
    public void testMySpringBeanLifecycle() {
        String message = "Hello World";
        mySpringBean.sendMessage(message);
        assertThat(mySpringBean.getMessage()).isEqualTo(message);
    }

}
