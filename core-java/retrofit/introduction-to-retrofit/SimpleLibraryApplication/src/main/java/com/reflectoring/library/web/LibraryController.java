package com.reflectoring.library.web;

import com.reflectoring.library.model.Response;
import com.reflectoring.library.model.Status;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("library/managed/books")
public class LibraryController {

    private static final Logger log = LoggerFactory.getLogger(LibraryController.class);

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getBooks() {
        return ResponseEntity.ok().body(libraryService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Response> createBook(@RequestBody BookDto book) {
        return ResponseEntity.ok().body(libraryService.createBook(book));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response> updateBook(@PathVariable Long id, @RequestBody BookDto book) {
        return ResponseEntity.ok().body(libraryService.updateBook(id, book));
    }

    @DeleteMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<Response> deleteBook(@PathVariable Long id) {
        Response response = libraryService.deleteBook(id);
        log.info("Response received : {}", response);
        log.info("Status from response : {}", Status.fetchCode(response.getResponseCode()));
        return ResponseEntity.status(Status.fetchCode(response.getResponseCode())).body(response);
    }
}
