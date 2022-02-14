package com.reflectoring.begginersguide.services;

import com.reflectoring.begginersguide.controllers.dto.BookRequest;
import com.reflectoring.begginersguide.controllers.dto.BookResponse;
import com.reflectoring.begginersguide.domain.Book;
import com.reflectoring.begginersguide.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateBookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public CreateBookService(BookRepository bookRepository,
                             BookMapper getBookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = getBookMapper;
    }

    public BookResponse createBook(BookRequest request){
        Book book = bookMapper.map(request);
        return bookMapper.map(bookRepository.save(book));
    }

}
