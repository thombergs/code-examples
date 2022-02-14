package com.reflectoring.begginersguide.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue( strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,
//            generator = "book_generator")
//    @SequenceGenerator(name = "book_generator",
//            sequenceName = "book_seq",
//            initialValue = 10)
//    @GeneratedValue(strategy = GenerationType.TABLE,
//            generator = "book_generator")
//    @TableGenerator(name = "book_generator", table = "book_id_table")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publication")
    private Date publication;

    @Column(name = "numberOfInstances")
    private int numberOfInstances;

    @ManyToMany(mappedBy = "borrowedBooks")
    private List<User> users;

    public Book() {
    }

    public Book(long id, String title, String author,
                Date publication, int numberOfInstances,
                List<User> users) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.numberOfInstances = numberOfInstances;
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public Book id(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public Date getPublication() {
        return publication;
    }

    public Book publication(Date publication) {
        this.publication = publication;
        return this;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    public Book numberOfInstances(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Book users(
            List<User> users) {
        this.users = users;
        return this;
    }
}
