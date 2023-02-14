package com.reflectoring.security.web;

import com.reflectoring.security.exception.UserAuthenticationErrorHandler;
import com.reflectoring.security.mapstruct.BookDto;
import com.reflectoring.security.model.LibraryInfo;
import com.reflectoring.security.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/library/books")
    @PreAuthorize("#user == authentication.principal.username")
    public ResponseEntity<List<BookDto>> getBooks(@RequestParam String genre, @RequestParam String user) {
        return ResponseEntity.ok().body(bookService.getBook(genre));
    }

    @GetMapping("/library/books/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok().body(bookService.getAllBooks());
    }

    @GetMapping("/library/info")
    public ResponseEntity<LibraryInfo> getInfo() {
        return ResponseEntity.ok().body(bookService.getLibraryInfo());
    }


}
