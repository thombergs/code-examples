package io.reflectoring.modules.github;

import io.reflectoring.modules.github.internal.GitHubService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@TestConfiguration
public class GitHubModuleMock {

    private final GitHubService gitHubServiceMock = Mockito.mock(GitHubService.class);

    @Bean
    @Primary
    GitHubService gitHubServiceMock() {
        return gitHubServiceMock;
    }

    public void givenCreateRepositoryReturnsUrl(String url) {
        given(gitHubServiceMock.createRepository(any(), any())).willReturn(url);
    }

    public void givenRepositoryExists(){
        given(gitHubServiceMock.repositoryExists(anyString(), anyString(), anyString())).willReturn(true);
    }

    public void givenRepositoryDoesNotExist(){
        given(gitHubServiceMock.repositoryExists(anyString(), anyString(), anyString())).willReturn(false);
    }

    public void assertRepositoryCreated(){
        verify(gitHubServiceMock).createRepository(any(), any());
    }

    public void givenDefaultState(String defaultRepositoryUrl){
        givenRepositoryDoesNotExist();
        givenCreateRepositoryReturnsUrl(defaultRepositoryUrl);
    }

    public void assertRepositoryNotCreated(){
        verify(gitHubServiceMock, never()).createRepository(any(), any());
    }

}
