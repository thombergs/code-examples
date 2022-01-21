package com.reflectoring.io.immutables.optionalAndDefault;

import org.immutables.value.Value;

@Value.Immutable
abstract class DefaultArticle {

    abstract Long getId();

    @Value.Default
    String getTitle(){
        return "Default title!";
    }

    abstract String getContent();

}
