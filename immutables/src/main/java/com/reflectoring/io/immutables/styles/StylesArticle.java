package com.reflectoring.io.immutables.styles;

import org.immutables.value.Value;

@Value.Immutable
@CustomStyle
abstract class StylesArticle {
    abstract long getId();

    abstract String getTitle();

    abstract String getContent();
}
