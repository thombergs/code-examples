package io.reflectoring.modules.github.api;

import java.util.Objects;

public class GitHubRepository {

    private final  String name;
    private final String organization;

    public GitHubRepository(String name, String organization) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(organization);
        this.name = name;
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }
}
