package com.reflectoring.io.immutables.builders;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(stagedBuilder = true)
abstract class StagedBuilderArticle {

    abstract long getId();

    abstract String getTitle();

    abstract String getContent();
}
