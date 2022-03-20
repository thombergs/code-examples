package com.reflectoring.beginnersguide.controllers;

import com.reflectoring.beginnersguide.controllers.dto.BookRequest;
import com.reflectoring.beginnersguide.controllers.dto.BookResponse;
import com.reflectoring.beginnersguide.services.CreateBookService;
import com.reflectoring.beginnersguide.services.DeleteBookService;
import com.reflectoring.beginnersguide.services.UpdateBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/books")
public class AdminBooksRestController {

    private final CreateBookService createBookService;

    private final UpdateBookService updateBookService;

    private final DeleteBookService deleteBookService;

    @Autowired
    public AdminBooksRestController(CreateBookService createBookService,
                                    UpdateBookService updateBookService,
                                    DeleteBookService deleteBookService) {
        this.createBookService = createBookService;
        this.updateBookService = updateBookService;
        this.deleteBookService = deleteBookService;
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
