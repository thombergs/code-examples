package com.reflectoring.io.immutables.basic;


import org.immutables.value.Value;

import java.util.List;


@Value.Immutable
public abstract class User {

    abstract long getId();

    abstract String getName();

    abstract String getLastname();

    abstract String getEmail();

    abstract  String getPassword();

    abstract List<Article> getArticles();
}
