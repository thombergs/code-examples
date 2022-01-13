package com.reflectoring.io.immutables.derivedAndLazy;

public class DerivedAndLazyService {

    public static void main(String[] args) {
        DerivedArticle article = ImmutableDerivedArticle.builder()
                .id(1l)
                .title("Lorem ipsum")
                .content("Lorem ipsum dolor sit amet, consectetur " +
                        "adipiscing elit. Donec ut sem lectus. " +
                        "Nulla dapibus eros.")
                .build();

        System.out.println(article);
    }
}
