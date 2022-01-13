package com.reflectoring.io.immutables.withoutImmutables;

import java.util.Objects;

public class ArticleWithoutImmutable {

    private final long id;

    private final String title;

    private final String content;

    private final long userId;

    private ArticleWithoutImmutable(long id, String title,
                                    String content, long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleWithoutImmutable that = (ArticleWithoutImmutable) o;
        return id == that.id && Objects.equals(title, that.title) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }

    public static ArticleWithoutImmutableBuilder builder() {
        return new ArticleWithoutImmutableBuilder();
    }

    public static class ArticleWithoutImmutableBuilder {
        private long id;

        private String title;

        private String content;

        private long userId;

        public ArticleWithoutImmutableBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ArticleWithoutImmutableBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleWithoutImmutableBuilder content(
                String content) {
            this.content = content;
            return this;
        }

        public ArticleWithoutImmutableBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ArticleWithoutImmutable build() {
            return new ArticleWithoutImmutable(id, title, content,
                    userId);
        }
    }
}
