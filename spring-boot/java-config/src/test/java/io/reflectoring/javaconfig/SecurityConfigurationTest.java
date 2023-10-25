package io.reflectoring.javaconfig;

import io.reflectoring.javaconfig.controllers.EmployeeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
@ContextConfiguration(classes = {SecurityConfiguration.class, JavaConfigAppConfiguration.class})
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mvc;


    @WithMockUser(authorities = "USER")
    @Test
    void endpointWhenUserAuthorityThenAuthorized() throws Exception {
        mvc.perform(get("/employees/1")).andExpect(status().isOk());
    }

    @WithMockUser(authorities = "USER")
    @Test
    void endpointWhenNotUserAuthorityThenForbidden() throws Exception {
        mvc.perform(get("/organizations/1")).andExpect(status().isForbidden());
    }
}