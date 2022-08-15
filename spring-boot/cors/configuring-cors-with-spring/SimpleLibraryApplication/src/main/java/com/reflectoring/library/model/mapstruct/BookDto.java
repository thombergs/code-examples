package com.reflectoring.library.model.mapstruct;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
public class BookDto {
    @JsonProperty("bookId")
    private long id;

    @JsonProperty("bookName")
    private String name;

    @JsonProperty("publisher")
    private String publisher;

    @JsonProperty("publicationYear")
    private String publicationYear;

    @JsonProperty("isCopyrighted")
    private boolean copyrightIssued;

    @JsonProperty("authors")
    private Set<AuthorDto> authors;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isCopyrightIssued() {
        return copyrightIssued;
    }

    public void setCopyrightIssued(boolean copyrightIssued) {
        this.copyrightIssued = copyrightIssued;
    }

    public Set<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDto> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", copyrightIssued=" + copyrightIssued +
                ", authors=" + authors +
                '}';
    }
}
