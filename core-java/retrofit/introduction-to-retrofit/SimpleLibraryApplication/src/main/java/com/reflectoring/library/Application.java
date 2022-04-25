package com.reflectoring.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.model.persistence.Author;
import com.reflectoring.library.model.persistence.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        /*Book book = new Book();
        book.setName("Book1");
        book.setPublisher("Publisher1");
        book.setPublicationYear("2009");
        book.setCopyrightIssued(true);
        Author author = new Author();
        author.setName("Author1");
        author.setDob("01/07/1987");
        book.setAuthors(Set.of(author));
        try {
            System.out.println(new ObjectMapper().writeValueAsString(book));
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }*/

    }

}
