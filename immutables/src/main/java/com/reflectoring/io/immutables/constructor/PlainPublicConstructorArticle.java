package com.reflectoring.io.immutables.constructor;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(of = "new")
public abstract class PlainPublicConstructorArticle {
    @Value.Parameter
    public abstract long getId();
    @Value.Parameter
    public abstract String getTitle();
    @Value.Parameter
    public abstract String getContent();
}
