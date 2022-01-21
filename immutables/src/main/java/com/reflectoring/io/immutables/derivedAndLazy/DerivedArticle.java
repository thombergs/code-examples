package com.reflectoring.io.immutables.derivedAndLazy;

import org.immutables.value.Value;

@Value.Immutable
abstract class DerivedArticle {

    abstract Long getId();

    abstract String getTitle();

    abstract String getContent();

    @Value.Derived
    String getSummary(){
        String summary = getContent().substring(0,
                getContent().length()>50 ? 50 :
                        getContent().length());
        return summary.length() == getContent().length() ? summary
                : summary+"...";
    }
}
