package com.reflectoring.io.immutables.optionalAndDefault;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
abstract class OptionalArticle {

    abstract Optional<Long> getId();

    abstract Optional<String> getTitle();

    abstract Optional<String> getContent();
}
