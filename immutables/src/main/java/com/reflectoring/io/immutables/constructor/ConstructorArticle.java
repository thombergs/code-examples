package com.reflectoring.io.immutables.constructor;

import org.immutables.value.Value;

@Value.Immutable
public abstract class ConstructorArticle {
    @Value.Parameter
    public abstract long getId();
    @Value.Parameter
    public abstract String getTitle();
    @Value.Parameter
    public abstract String getContent();
}
