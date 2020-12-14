package io.reflectoring.modules.github.internal;

import io.reflectoring.modules.github.api.GitHubMutations;
import io.reflectoring.modules.github.api.GitHubQueries;
import io.reflectoring.modules.github.api.GitHubRepository;

import java.util.Collections;
import java.util.List;

public class GitHubService implements GitHubMutations, GitHubQueries {

    @Override
    public String createRepository(String token, GitHubRepository repository) {
        // call the GitHub API to create a repo
        return "https://github.com/reflectoring/reflectoring.github.io";
    }

    @Override
    public List<String> getOrganisations(String token) {
        // call the GitHub API to get a list of organisations for the user
        return Collections.emptyList();
    }

    @Override
    public List<String> getRepositories(String token, String organisation) {
        // call the GitHub API to get a list of repositories for the user
        return Collections.emptyList();
    }

    @Override
    public boolean repositoryExists(String token, String repositoryName, String organisation) {
        // call the GitHub API to find out if a given repo exists
        return false;
    }
}
