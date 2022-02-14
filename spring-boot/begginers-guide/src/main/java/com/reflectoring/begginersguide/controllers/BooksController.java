package com.reflectoring.begginersguide.controllers;

import com.reflectoring.begginersguide.controllers.dto.BookResponse;
import com.reflectoring.begginersguide.services.GetBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/controllerBooks")
public class BooksController {

    private final GetBookService getBookService;

    @Autowired
    public BooksController(GetBookService getBookService) {
        this.getBookService = getBookService;
    }

    @GetMapping
    ResponseEntity<List<BookResponse>> fetchAllBooks(){
        return ResponseEntity.ok(getBookService.getAllBooks());
    }
}
