package io.reflectoring.modules.github.api;

import java.util.List;

public interface GitHubQueries {

    List<String> getOrganisations(String token);

    List<String> getRepositories(String token, String organisation);

    boolean repositoryExists(String token, String repositoryName, String organisation);

}
