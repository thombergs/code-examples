package com.reflectoring.io.immutables.builders;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
abstract class StrictBuilderArticle {
    abstract long getId();

    abstract String getTitle();

    abstract String getContent();
}
