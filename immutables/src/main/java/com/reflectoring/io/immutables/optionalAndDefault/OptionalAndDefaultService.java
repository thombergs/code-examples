package com.reflectoring.io.immutables.optionalAndDefault;

public class OptionalAndDefaultService {

    //Explain optional and how we can omit values
    //Explain default and how we use it
    public static void main(String[] args) {
        OptionalArticle article =  ImmutableOptionalArticle.builder().build();
        DefaultArticle defaultArticle = ImmutableDefaultArticle.builder()
                .id(0l)
                .content("Lorem ipsum!")
                .build();

        DefaultArticleInterface defaultArticleInterface =
                ImmutableDefaultArticleInterface.builder()
                        .id(0l)
                        .content("Lorem ipsum!")
                        .build();

        System.out.println(defaultArticle);
        System.out.println(defaultArticleInterface);
    }
}
