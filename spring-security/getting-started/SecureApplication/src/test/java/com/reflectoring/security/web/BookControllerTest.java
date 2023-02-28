package com.reflectoring.security.web;

import com.reflectoring.security.config.BasicAuthProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "classpath:init/first.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/second.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "bookadmin", roles = {"USER"})
    void successIfSecurityApplies() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fiction")
                        .param("user", "bookadmin")
                        .header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("bookadmin"))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(jsonPath("$", hasSize(3)))
        ;
    }

    @Test
    @WithMockUser(username = "bookadmin", roles = {"ADMIN"})
    void failsForWrongAuthorization() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fiction")
                        .param("user", "bookadmin")
                        .header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    void failsIfSecurityApplies() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fiction")
                        .param("user", "bookadmin")
                        .header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithUserDetails(value="bookadmin", userDetailsServiceBeanName="userDetailsService")
    void testBookWithConfiguredUserDetails() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fantasy")
                        .param("user", "bookadmin")
                        .header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
        ;
    }

    @Test
    @WithUserDetails(value="bookadmin", userDetailsServiceBeanName="userDetailsService")
    void failsIfMandatoryHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fantasy")
                        .param("user", "bookadmin"))
                        //.header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @WithUserDetails(value="bookadmin", userDetailsServiceBeanName="userDetailsService")
    void failsIfPreAuthorizeConditionFails() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fantasy")
                        .param("user", "bookuser")
                .header("X-Application-Name", "Library"))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    //@WithUserDetails(value="bookadmin", userDetailsServiceBeanName="userDetailsService")
    void testBookWithWrongCredentialsUserDetails() throws Exception {
        mockMvc.perform(get("/library/books")
                        .param("genre", "Fantasy")
                        .param("user", "bookadmin")
                        .header("X-Application-Name", "Library")
                        .with(httpBasic("bookadmin", "password")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
