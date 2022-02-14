package com.reflectoring.begginersguide.controllers.dto;

public class BookResponse {
    private long id;
    private String title;
    private String author;
    private String publishedOn;
    private long currentlyAvailableNumber;

    private BookResponse(long id, String title, String author,
                         String publishedOn,
                         long currentlyAvailableNumber) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishedOn = publishedOn;
        this.currentlyAvailableNumber = currentlyAvailableNumber;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public long getCurrentlyAvailableNumber() {
        return currentlyAvailableNumber;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private long id;
        private String title;
        private String author;
        private String publishedOn;
        private long currentlyAvailableNumber;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder publishedOn(String publishedOn) {
            this.publishedOn = publishedOn;
            return this;
        }

        public Builder currentlyAvailableNumber(
                long currentlyAvailableNumber) {
            this.currentlyAvailableNumber = currentlyAvailableNumber;
            return this;
        }

        public BookResponse build(){
            return new BookResponse(id, title, author, publishedOn,
                    currentlyAvailableNumber);
        }
    }
}
