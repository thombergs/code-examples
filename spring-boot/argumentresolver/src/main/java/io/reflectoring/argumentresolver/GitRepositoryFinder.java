package io.reflectoring.argumentresolver;

import java.util.Optional;

public interface GitRepositoryFinder {

  Optional<GitRepository> findBySlug(String slug);

}
