package com.reflectoring.begginersguide.services;

import com.reflectoring.begginersguide.controllers.dto.BookRequest;
import com.reflectoring.begginersguide.controllers.dto.BookResponse;
import com.reflectoring.begginersguide.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateBookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public UpdateBookService(BookRepository bookRepository,
                             BookMapper getBookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = getBookMapper;
    }

    public BookResponse updateBook(long id, BookRequest request){
        return null;
    }

}
