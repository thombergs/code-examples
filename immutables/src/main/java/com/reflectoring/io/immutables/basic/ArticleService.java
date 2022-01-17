package com.reflectoring.io.immutables.basic;

import java.util.List;

public class ArticleService {

    // Explain how to create Immutable object the simplest way possible.
    // Show generated class and class that we created
    public static void main(String[] args) {
        Article article = ImmutableArticle.builder()
                .id(0)
                .content("Lorem ipsum dolor sit amet.")
                .title("Lorem ipsum article!")
                .userId(1l)
                .build();

        User user = ImmutableUser.builder()
                .id(1l)
                .name("Mateo")
                .lastname("Stjepanovic")
                .email("mateo@mock.com")
                .password("password")
                .articles(List.of(article))
                .build();
        System.out.println(user);
    }
}
