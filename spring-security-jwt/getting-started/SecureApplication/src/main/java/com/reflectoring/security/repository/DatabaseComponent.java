package com.reflectoring.security.repository;

import com.reflectoring.security.persistence.Author;
import com.reflectoring.security.persistence.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseComponent implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Autowired
    public DatabaseComponent(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Book book = new Book();
        book.setName("The Kite Runner");
        book.setPublisher("Riverhead books");
        book.setPublicationYear("2003");
        book.setGenre("Fiction");
        Author author = new Author();
        author.setName("Khaled Hosseini");
        author.setDob("04/03/1965");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

        book = new Book();
        book.setName("Exiles");
        book.setPublisher("Pan Macmillan");
        book.setPublicationYear("2022");
        book.setGenre("Fiction");
        author = new Author();
        author.setName("Jane Harper");
        author.setDob("01/06/1980");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

        book = new Book();
        book.setName("A Game of Thrones");
        book.setPublisher("Bantam Spectra");
        book.setPublicationYear("1996");
        book.setGenre("Fantasy");
        author = new Author();
        author.setName("R.R.Martin");
        author.setDob("20/09/1948");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

        book = new Book();
        book.setName("American Gods");
        book.setPublisher("Headline");
        book.setPublicationYear("2001");
        book.setGenre("Fantasy");
        author = new Author();
        author.setName("Neil Gaiman");
        author.setDob("10/11/1960");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

        book = new Book();
        book.setName("The Passenger");
        book.setPublisher("Knopf");
        book.setPublicationYear("2022");
        book.setGenre("Mystery");
        author = new Author();
        author.setName("Cormac McCarthy");
        author.setDob("20/07/1933");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

        book = new Book();
        book.setName("Gone Girl");
        book.setPublisher("Crown Publishing Group");
        book.setPublicationYear("2012");
        book.setGenre("Mystery");
        author = new Author();
        author.setName("Gillian Flynn");
        author.setDob("24/02/1971");
        book.setAuthors(Set.of(author));
        bookRepository.save(book);

    }
}
