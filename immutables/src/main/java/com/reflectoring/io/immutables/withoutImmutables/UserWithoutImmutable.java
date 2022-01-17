package com.reflectoring.io.immutables.withoutImmutables;


import com.reflectoring.io.immutables.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserWithoutImmutable {

    private final long id;

    private final String name;

    private final String lastname;

    private final String email;

    private final String password;

    private final Role role;

    private List<ArticleWithoutImmutable> articles;

    private UserWithoutImmutable(long id, String name,
                                 String lastname, String email,
                                 String password, Role role,
                                 List<ArticleWithoutImmutable> articles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.articles = new ArrayList<>(articles);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public List<ArticleWithoutImmutable> getArticles() {
        return articles;
    }

    public UserWithoutImmutable addArticle(
            ArticleWithoutImmutable article) {
        this.articles.add(article);
        return this;
    }

    public UserWithoutImmutable addArticles(
            List<ArticleWithoutImmutable> articles) {
        this.articles.addAll(articles);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWithoutImmutable that = (UserWithoutImmutable) o;
        return id == that.id && email.equals(that.email) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    @Override
    public String toString() {
        return "UserWithoutImmutable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role= '" + role + '\'' +
                ", email='" + email + '\'' +
                ", password= *****'" +
                ", articles=" + articles +
                '}';
    }

    public static UserWithoutImmutableBuilder builder() {
        return new UserWithoutImmutableBuilder();
    }

    public static class UserWithoutImmutableBuilder {
        private long id;

        private String name;

        private String lastname;

        private Role role;

        private String email;

        private String password;

        private List<ArticleWithoutImmutable> articles;

        public UserWithoutImmutableBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UserWithoutImmutableBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserWithoutImmutableBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserWithoutImmutableBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserWithoutImmutableBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserWithoutImmutableBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserWithoutImmutableBuilder articles(
                List<ArticleWithoutImmutable> articles) {
            this.articles = articles;
            return this;
        }

        public UserWithoutImmutable build() {
            return new UserWithoutImmutable(id, name, lastname, email,
                    password, role, articles);
        }
    }
}
