package com.reflectoring.io.immutables.optionalAndDefault;

import org.immutables.value.Value;

@Value.Immutable
interface DefaultArticleInterface {

    Long getId();

    @Value.Default
    default String getTitle(){
        return "Default title!";
    }

    String getContent();

}
