package com.reflectoring.delombok;

import com.reflectoring.lombok.model.Author;
import com.reflectoring.lombok.model.Genre;

import java.util.List;

public class Book {
    private String isbn;

    private String publication;

    private String title;

    private List<Author> authors;

    private final Genre genre = Genre.FICTION;

    public Book(String isbn, String publication, String title, List<Author> authors) {
        this.isbn = isbn;
        this.publication = publication;
        this.title = title;
        this.authors = authors;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getPublication() {
        return this.publication;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Author> getAuthors() {
        return this.authors;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String toString() {
        return "Book(isbn=" + this.getIsbn() + "," +
                " publication=" + this.getPublication() +
                ", title=" + this.getTitle() + ", " +
                "authors=" + this.getAuthors() + ", " +
                "genre=" + this.getGenre() + ")";
    }
}
