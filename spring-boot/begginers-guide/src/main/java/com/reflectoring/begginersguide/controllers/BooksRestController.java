package com.reflectoring.begginersguide.controllers;

import com.reflectoring.begginersguide.controllers.dto.BookRequest;
import com.reflectoring.begginersguide.controllers.dto.BookResponse;
import com.reflectoring.begginersguide.services.CreateBookService;
import com.reflectoring.begginersguide.services.DeleteBookService;
import com.reflectoring.begginersguide.services.GetBookService;
import com.reflectoring.begginersguide.services.UpdateBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksRestController {

    private final GetBookService getBookService;

    private final CreateBookService createBookService;

    private final UpdateBookService updateBookService;

    private final DeleteBookService deleteBookService;

    @Autowired
    public BooksRestController(GetBookService getBookService,
                               CreateBookService createBookService,
                               UpdateBookService updateBookService,
                               DeleteBookService deleteBookService) {
        this.getBookService = getBookService;
        this.createBookService = createBookService;
        this.updateBookService = updateBookService;
        this.deleteBookService = deleteBookService;
    }

    @GetMapping
    List<BookResponse> fetchAllBooks(){
        return getBookService.getAllBooks();
    }

    @PostMapping
    BookResponse create(@RequestBody BookRequest request){
        return createBookService.createBook(request);
    }

    @PutMapping("/{id}")
    BookResponse update(@PathVariable("id") long id,
                        @RequestBody BookRequest request){
        return updateBookService.updateBook(id,request);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") long id){
        deleteBookService.delete(id);
    }

}
