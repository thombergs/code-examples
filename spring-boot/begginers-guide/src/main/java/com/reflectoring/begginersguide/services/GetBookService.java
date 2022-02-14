package com.reflectoring.begginersguide.services;

import com.reflectoring.begginersguide.controllers.dto.BookResponse;
import com.reflectoring.begginersguide.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBookService {

    private final BookRepository bookRepository;

//    @Autowired
//    public GetBookService(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
    private final BookMapper getBookMapper;

    public GetBookService(BookRepository bookRepository,
                          BookMapper getBookMapper) {
        this.bookRepository = bookRepository;
        this.getBookMapper = getBookMapper;
    }

//    @Autowired
//    private void setBookRepository(BookRepository bookRepository){
//        this.bookRepository = bookRepository;
//    }

    public List<BookResponse> getAllBooks(){
        return getBookMapper.mapAll(bookRepository.findAll());
    }

    public List<BookResponse> getAllBooksWhereInstancesGreaterThanFive(){
        return getBookMapper.mapAll(bookRepository.findWithMoreInstancesThenFive());
    }
}
