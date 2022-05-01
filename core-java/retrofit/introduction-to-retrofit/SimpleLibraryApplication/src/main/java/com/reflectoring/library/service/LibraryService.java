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

    public List<BookDto> getAllBooks(String type) {
        List<Book> allBooks;
        if ("all".equalsIgnoreCase(type)) {
            allBooks  = bookRepository.findAll();
        } else { //default return empty List
            log.error("Query Param 'type' not set to 'all'");
            allBooks  = Collections.emptyList();
        }

        log.info("Get All Books : {}", allBooks);
        return libraryMapper.bookToBookDto(allBooks);
    }

    public BookDto getOneBook(String bookId) {
        Book book = null;
        Optional<Book> optBook = bookRepository.findById(Long.parseLong(bookId));
        if (optBook.isPresent()) {
            book = optBook.get();
        }

        log.info("Get One book : {}", book);
        return libraryMapper.bookToBookDto(book);
    }

    public Response createBook(BookDto bookDto) {
        log.info("Book DTO from request : {}", bookDto);
        Book book = libraryMapper.bookDtoToBook(bookDto);
        log.info("Mapping from BookDTO to Book entity is done. Book : {}", book);
        bookRepository.save(book);
        return new Response(Status.SUCCESS.toString(), "Book created successfully");
    }

    public Response updateBook(Long id, BookDto bookdto) {
        Book book = libraryMapper.bookDtoToBook(bookdto);
        log.info("To be updated Book data: {}", book);
        Book fetchBook = bookRepository.getOne(id);
        log.info("Before update first fetch Book details from DB. Book: {}", fetchBook);
        if (Objects.nonNull(book.getAuthors()) && !book.getAuthors().isEmpty()) {
            fetchBook.getAuthors().clear();
            fetchBook.setAuthors(book.getAuthors());
        }
        if (Objects.nonNull(book.isCopyrightIssued())) {
            fetchBook.setCopyrightIssued(book.isCopyrightIssued());
        }
        if (Objects.nonNull(book.getPublisher())) {
            fetchBook.setPublisher(book.getPublisher());
        }
        if (Objects.nonNull(book.getPublicationYear())) {
            fetchBook.setPublicationYear(book.getPublicationYear());
        }
        log.info("Updated book object is {}", fetchBook);
        bookRepository.save(fetchBook);
        return new Response(Status.SUCCESS.toString(), "Book updated successfully");
    }

    public Response deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return new Response(Status.SUCCESS.toString(), "Book deleted successfully");
        } else {
            return new Response(Status.ERROR.toString(), "Could not delete book for id : " + id);
        }
    }
}
