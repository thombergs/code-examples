package com.reflectoring.library.model.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table("BOOK")
public class Book implements Serializable {
    @Id
    @Column("id")
    private long id;

    @Column("name")
    private String name;

    @Column("publisher")
    private String publisher;

    @Column("publicationYear")
    private String publicationYear;

    @Column("authorName")
    private String authorName;

    public Book(String name, String publisher, String publicationYear, String authorName) {
        this.name = name;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.authorName = authorName;
    }

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

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", authorName=" + authorName +
                '}';
    }
}
