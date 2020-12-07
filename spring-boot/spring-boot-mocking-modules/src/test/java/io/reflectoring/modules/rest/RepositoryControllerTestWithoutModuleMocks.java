package io.reflectoring.modules.rest;

import io.reflectoring.modules.github.api.GitHubMutations;
import io.reflectoring.modules.github.api.GitHubQueries;
import io.reflectoring.modules.mail.api.EmailNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestPropertySource(properties = "mail.enabled=false")
class RepositoryControllerTestWithoutModuleMocks {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubMutations gitHubMutations;

    @MockBean
    private GitHubQueries gitHubQueries;

    @MockBean
    private EmailNotificationService emailNotificationService;

    @Test
    void givenRepositoryDoesNotExist_thenRepositoryIsCreatedSuccessfully() throws Exception {

        String repositoryUrl = "https://github.com/reflectoring/reflectoring.github.io";

        given(gitHubQueries.repositoryExists(
                anyString(),
                anyString(),
                anyString())
        ).willReturn(false);

        given(gitHubMutations.createRepository(
                any(),
                any())
        ).willReturn(repositoryUrl);

        mockMvc.perform(post("/github/repository")
                .param("token", "123")
                .param("repositoryName", "foo")
                .param("organizationName", "bar"))
                .andExpect(status().is(200));

        verify(emailNotificationService).sendEmail(
                anyString(),
                anyString(),
                contains(repositoryUrl)
        );

        verify(gitHubMutations).createRepository(
                any(),
                any()
        );
    }

}