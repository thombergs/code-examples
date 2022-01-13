package com.reflectoring.io.immutables.basic;


import org.immutables.value.Value;

import java.util.List;


@Value.Immutable
public abstract class User {

    public abstract long getId();

    public abstract String getName();

    public abstract String getLastname();

    public abstract String getEmail();

    public abstract  String getPassword();

    public abstract List<Article> getArticles();
}
