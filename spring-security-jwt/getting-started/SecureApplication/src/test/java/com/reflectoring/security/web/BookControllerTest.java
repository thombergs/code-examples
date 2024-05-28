package com.reflectoring.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.security.model.TokenRequest;
import com.reflectoring.security.model.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void failsAsBearerTokenNotSet() throws Exception {
        mockMvc.perform(get("/library/books/all"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithValidBearerToken() throws Exception {
        TokenRequest request = TokenRequest.builder()
                .username("libUser")
                .password("libPassword")
                .build();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/token/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();
        String resultStr = mvcResult.getResponse().getContentAsString();
        TokenResponse token = new ObjectMapper().readValue(resultStr, TokenResponse.class);
        mockMvc.perform(get("/library/books/all")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void testWithInvalidBearerToken() throws Exception {
        mockMvc.perform(get("/library/books/all")
                        .header("Authorization", "Bearer 123"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
