package com.reflectoring.beginnersguide.controllers.dto;

public class BookRequest {
    private String title;
    private String author;
    private String publishedOn;
    private long currentlyAvailableNumber;

    private BookRequest(String title, String author,
                         String publishedOn,
                         long currentlyAvailableNumber) {
        this.title = title;
        this.author = author;
        this.publishedOn = publishedOn;
        this.currentlyAvailableNumber = currentlyAvailableNumber;
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
        private String title;
        private String author;
        private String publishedOn;
        private long currentlyAvailableNumber;

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

        public BookRequest build(){
            return new BookRequest(title, author, publishedOn,
                    currentlyAvailableNumber);
        }
    }
}
