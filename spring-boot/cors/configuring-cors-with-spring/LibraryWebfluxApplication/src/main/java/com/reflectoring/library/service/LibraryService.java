package com.reflectoring.library.service;

import com.reflectoring.library.mapper.LibraryMapper;
import com.reflectoring.library.model.Response;
import com.reflectoring.library.model.Status;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.model.persistence.Book;
import com.reflectoring.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LibraryService {

    private static final Logger log = LoggerFactory.getLogger(LibraryService.class);

    private final BookRepository bookRepository;

    private final LibraryMapper libraryMapper;

    public LibraryService(BookRepository bookRepository, LibraryMapper libraryMapper) {
        this.bookRepository = bookRepository;
        this.libraryMapper = libraryMapper;
    }

    public Mono<List<BookDto>> getAllBooks(String type) {
        Flux<Book> allBooks = null;
        System.out.println(type);
        allBooks  = bookRepository.findAll();
        Mono<List<BookDto>> bookList = allBooks.map(book -> libraryMapper.bookToBookDto(book)).collectList();
        return bookList;
    }

    public Mono<BookDto> getOneBook(String bookId) {
        Mono<Book> book = bookRepository.findById(Long.parseLong(bookId));

        log.info("Get One book : {}", book);
        return book.map(x -> libraryMapper.bookToBookDto(x));
    }

    public Response createBook(BookDto bookDto) {
        log.info("Book DTO from request : {}", bookDto);
        Book book = libraryMapper.bookDtoToBook(bookDto);
        log.info("Mapping from BookDTO to Book entity is done. Book : {}", book);
        bookRepository.save(book);
        return new Response(Status.SUCCESS.toString(), "Book created successfully");
    }
}
