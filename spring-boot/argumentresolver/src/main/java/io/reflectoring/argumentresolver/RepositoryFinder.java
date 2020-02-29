package io.reflectoring.argumentresolver;

import java.util.Optional;

public interface RepositoryFinder {

  Optional<Repository> findBySlug(String slug);

}
