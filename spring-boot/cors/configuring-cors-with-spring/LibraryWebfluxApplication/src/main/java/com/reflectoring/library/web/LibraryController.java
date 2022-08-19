package com.reflectoring.library.web;

import com.reflectoring.library.model.Response;
import com.reflectoring.library.model.Status;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("cors-library/managed/books")
public class LibraryController {

    private static final Logger log = LoggerFactory.getLogger(LibraryController.class);

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Requestor-Type", "Authorization"}, exposedHeaders = "X-Get-Header")
    @GetMapping
    public ResponseEntity<Mono<List<BookDto>>> getBooks(@RequestParam String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Get-Header", "ExampleHeader");
        return ResponseEntity.ok().headers(headers).body(libraryService.getAllBooks(type));
    }

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/one")
    public ResponseEntity<Mono<BookDto>> getBooksWithHeaders(
            @RequestHeader(value = "requestId", required = false) String requestId) {
        log.info("Header values: {}", requestId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Book-Header", "BookHeader");
        if (Objects.nonNull(requestId)) {
            return ResponseEntity.ok().headers(headers).body(libraryService.getOneBook(requestId));
        } else {
            return ResponseEntity.ok().headers(headers).body(libraryService.getOneBook("1"));
        }

    }

    //@CrossOrigin(exposedHeaders = "X-Book-Header")
    @PostMapping
    public ResponseEntity<Response> createBook(@RequestBody BookDto book,
                                               @RequestHeader Map<String, String> requestHeaders) {
        System.out.println(requestHeaders);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Book-Header", "BookHeader");
        headers.set("X-Sample-Authcode", "Beaver123");
        return ResponseEntity.ok().headers(headers).body(libraryService.createBook(book));
    }
}
