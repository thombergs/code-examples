package com.reflectoring.io.immutables.collections;

import com.reflectoring.io.immutables.basic.Article;
import com.reflectoring.io.immutables.basic.ImmutableArticle;
import com.reflectoring.io.immutables.basic.ImmutableUser;
import com.reflectoring.io.immutables.basic.User;

public class CollectionsService {

    public static void main(String[] args) {

        Article article1 = ImmutableArticle.builder()
                .id(0l)
                .title("Lorem ipsum!")
                .content("Lorem ipsum")
                .userId(1l)
                .build();

        Article article2 = ImmutableArticle.builder()
                .id(2l)
                .title("Lorem ipsum!")
                .content("Lorem ipsum")
                .userId(1l)
                .build();

        Article article3 = ImmutableArticle.builder()
                .id(3l)
                .title("Lorem ipsum!")
                .content("Lorem ipsum")
                .userId(1l)
                .build();
        User user = ImmutableUser.builder()
                .id(1l)
                .name("Mateo")
                .lastname("Stjepanovic")
                .email("mock@mock.com")
                .password("mock")
                .addArticles(article1)
                .addArticles(article2)
                .build();
        System.out.println(user);

        user.getArticles().add(article3);

    }
}
