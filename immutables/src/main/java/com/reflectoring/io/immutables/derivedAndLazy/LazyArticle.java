package com.reflectoring.io.immutables.derivedAndLazy;

import org.immutables.value.Value;

@Value.Immutable
abstract class LazyArticle {

    abstract Long getId();
    
    abstract String getTitle();

    abstract String getContent();

    @Value.Lazy
    String summary(){
        String summary = getContent().substring(0,
                getContent().length()>50 ? 50 :
                        getContent().length());
        return summary.length() == getContent().length() ? summary
                : summary+"...";
    }

}
