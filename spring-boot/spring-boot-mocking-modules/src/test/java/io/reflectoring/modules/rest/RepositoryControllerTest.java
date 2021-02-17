package io.reflectoring.modules.rest;

import io.reflectoring.modules.github.GitHubModuleMock;
import io.reflectoring.modules.mail.EmailModuleMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestPropertySource(properties = "mail.enabled=false")
@Import({
        GitHubModuleMock.class,
        EmailModuleMock.class
})
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailModuleMock emailModuleMock;

    @Autowired
    private GitHubModuleMock gitHubModuleMock;

    @Test
    void givenRepositoryDoesNotExist_thenRepositoryIsCreatedSuccessfully() throws Exception {

        String repositoryUrl = "https://github.com/reflectoring/reflectoring.github.io";

        gitHubModuleMock.givenDefaultState(repositoryUrl);
        emailModuleMock.givenSendMailSucceeds();

        mockMvc.perform(post("/github/repository")
                .param("token", "123")
                .param("repositoryName", "foo")
                .param("organizationName", "bar"))
                .andExpect(status().is(200));

        emailModuleMock.assertSentMailContains(repositoryUrl);
        gitHubModuleMock.assertRepositoryCreated();
    }

    @Test
    void givenRepositoryExists_thenReturnsBadRequest() throws Exception {

        String repositoryUrl = "https://github.com/reflectoring/reflectoring.github.io";

        gitHubModuleMock.givenDefaultState(repositoryUrl);
        gitHubModuleMock.givenRepositoryExists();
        emailModuleMock.givenSendMailSucceeds();

        mockMvc.perform(post("/github/repository")
                .param("token", "123")
                .param("repositoryName", "foo")
                .param("organizationName", "bar"))
                .andExpect(status().is(400));

        emailModuleMock.assertNoMailSent();
        gitHubModuleMock.assertRepositoryNotCreated();
    }

}