package controller;

import com.reflectoring.csrf.controller.EmailController;
import com.reflectoring.csrf.controller.HomeController;
import com.reflectoring.csrf.service.CustomerEmailService;
import config.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EmailController.class, HomeController.class, SecurityConfiguration.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ControllerTest {
    @MockBean
    public CustomerEmailService customerEmailService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("password"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldLoginErrorWithInvalidCsrf() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf().useInvalidToken())
                        .param("username", "admin")
                        .param("password", "password"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void loadHomePageSuccessfully() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("testCookie"));
    }

    @Test
    @WithMockUser
    public void registerEmailSuccessfully() throws Exception {
        mockMvc.perform(post("/registerEmail").param("newEmail", "test@gmail.com")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void registerEmailErrorWithInvalidCsrf() throws Exception {
        mockMvc.perform(post("/registerEmail").param("newEmail", "test@gmail.com")
                        .with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
    }





}
