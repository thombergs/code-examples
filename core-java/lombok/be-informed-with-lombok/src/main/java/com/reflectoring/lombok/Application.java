package com.reflectoring.lombok;

import com.reflectoring.lombok.model.persistence.Book;
import com.reflectoring.lombok.model.persistence.Publisher;
import com.reflectoring.lombok.repository.BookRepository;
import com.reflectoring.lombok.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Add Data to tables - START
        Publisher publisher1 = Publisher.builder().id(5000).name("Birdie Publications").build();
        Publisher publisher2 = Publisher.builder().id(5001).name("KLM Publications").build();
        Publisher publisher3 = Publisher.builder().id(5002).name("Wallace Books").build();

        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
        publisherRepository.save(publisher3);

        Set<Publisher> pubList1 = Set.of(publisher1, publisher2);
        Book book1 = Book.builder().id(1000).name("BookA").publishers(pubList1).build();

        Set<Publisher> pubList2 = Set.of(publisher2, publisher3);
        Book book2 = Book.builder().id(1001).name("BookB").publishers(pubList2).build();

        bookRepository.save(book1);
        bookRepository.save(book2);
        // Add Data to tables - END
    }
}
