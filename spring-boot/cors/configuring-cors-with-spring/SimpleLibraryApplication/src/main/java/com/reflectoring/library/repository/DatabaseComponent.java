package com.reflectoring.library.repository;

import com.reflectoring.library.model.persistence.Author;
import com.reflectoring.library.model.persistence.Book;
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
        book.setName("Breakfast at Tiffany's");
        book.setPublisher("Harper's Bazaar");
        book.setPublicationYear("1958");
        book.setCopyrightIssued(true);
        Author author = new Author();
        author.setName("Truman Capote");
        author.setDob("30/09/1924");
        book.setAuthors(Set.of(author));

        bookRepository.save(book);

    }
}
