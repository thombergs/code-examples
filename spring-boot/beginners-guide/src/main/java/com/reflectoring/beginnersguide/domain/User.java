package com.reflectoring.beginnersguide.domain;

import javax.persistence.*;
import java.util.List;

@Entity(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "borrowed_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> borrowedBooks;

    public User() {
    }

    public User(long id, String name, String lastname,
                String email, String password,
                List<Book> borrowedBooks) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.borrowedBooks = borrowedBooks;
    }

    public long getId() {
        return id;
    }

    public User id(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public User borrowedBooks(
            List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
        return this;
    }
}
