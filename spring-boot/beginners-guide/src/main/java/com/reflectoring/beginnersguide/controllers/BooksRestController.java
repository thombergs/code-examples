package com.reflectoring.beginnersguide.controllers;

import com.reflectoring.beginnersguide.controllers.dto.BookResponse;
import com.reflectoring.beginnersguide.services.GetBookService;
import com.reflectoring.beginnersguide.services.UpdateBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksRestController {

    private final GetBookService getBookService;

    private final UpdateBookService updateBookService;

    @Autowired
    public BooksRestController(GetBookService getBookService,
                               UpdateBookService updateBookService) {
        this.getBookService = getBookService;
        this.updateBookService = updateBookService;
    }

    @GetMapping
    List<BookResponse> fetchAllBooks(){
        return getBookService.getAllBooks();
    }

    @PostMapping("/{id}/user/{userId}/borrow")
    void borrowABook(@PathVariable("id") long bookId,
                     @PathVariable("userId") long userId){
        updateBookService.borrow(bookId, userId);
    }

}
