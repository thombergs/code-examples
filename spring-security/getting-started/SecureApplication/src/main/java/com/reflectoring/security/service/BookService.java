package com.reflectoring.security.service;

import com.reflectoring.security.mapper.BookMapper;
import com.reflectoring.security.mapstruct.BookDto;
import com.reflectoring.security.model.LibraryInfo;
import com.reflectoring.security.persistence.Book;
import com.reflectoring.security.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> getBook(String genre) {
        List<Book> books = bookRepository.findByGenre(genre);
        return bookMapper.bookToBookDto(books);
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.bookToBookDto(books);
    }

    public LibraryInfo getLibraryInfo() {
        LibraryInfo info = new LibraryInfo();
        info.setLibName("Chatswood Library");
        info.setLibAddress("Lower Ground, The Concourse, 409 Victoria Avenue");
        info.setSuburb("Chatswood");
        info.setPostcode("2067");
        info.setLandmark("Opp Westfield Shopping Centre");
        info.setPhone("97777900");
        info.setEmail("library@willoughby.nsw.gov.au");

        return info;
    }
}
