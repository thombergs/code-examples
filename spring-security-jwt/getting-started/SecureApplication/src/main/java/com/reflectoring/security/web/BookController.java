package com.reflectoring.security.web;

import com.reflectoring.security.mapstruct.BookDto;
import com.reflectoring.security.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/library/books/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok().body(bookService.getAllBooks());
    }



}
