package io.reflectoring.modules.rest.internal;

import io.reflectoring.modules.github.api.GitHubMutations;
import io.reflectoring.modules.github.api.GitHubQueries;
import io.reflectoring.modules.github.api.GitHubRepository;
import io.reflectoring.modules.mail.api.EmailNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RepositoryController {

    private final GitHubMutations gitHubMutations;
    private final GitHubQueries gitHubQueries;
    private final EmailNotificationService emailNotificationService;

    public RepositoryController(
            GitHubMutations gitHubMutations,
            GitHubQueries gitHubQueries,
            EmailNotificationService emailNotificationService
    ) {
        this.gitHubMutations = gitHubMutations;
        this.gitHubQueries = gitHubQueries;
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping("/github/repository")
    ResponseEntity<Void> createGitHubRepository(
            @RequestParam("token") String token,
            @RequestParam("repositoryName") String repoName,
            @RequestParam("organizationName") String orgName
    ) {

        if (gitHubQueries.repositoryExists(token, repoName, orgName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String repoUrl = gitHubMutations.createRepository(token, new GitHubRepository(repoName, orgName));
        emailNotificationService.sendEmail("user@mail.com", "Your new repository", "Here's your new repository: " + repoUrl);

        return ResponseEntity.ok().build();
    }

}
