package com.reflectoring.io.immutables.basic;


import org.immutables.value.Value;


@Value.Immutable
public abstract class Article {

    abstract long getId();

    abstract String getTitle();

    abstract String getContent();

    abstract long getUserId();
}
