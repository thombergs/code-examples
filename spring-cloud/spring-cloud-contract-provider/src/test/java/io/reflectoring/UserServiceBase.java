package io.reflectoring;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public abstract class UserServiceBase {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setup() {
        User savedUser = new User();
        savedUser.setFirstName("Arthur");
        savedUser.setLastName("Dent");
        savedUser.setId(42L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        when(userRepository.findById(eq(42L))).thenReturn(Optional.of(savedUser));
    }

}