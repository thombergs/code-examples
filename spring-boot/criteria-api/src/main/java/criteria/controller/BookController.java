package criteria.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

//    private final BookRepository bookRepository;
//    private final BookService bookService;
//
//    public BookController(BookRepository bookRepository, BookService bookService) {
//        this.bookRepository = bookRepository;
//        this.bookService = bookService;
//    }
//
//    @GetMapping("/all/{authorName}/{bookName}")
//    public List<Book> getBooksByAuthorAndName(@PathVariable("authorName") String authorName, @PathVariable("bookName") String bookName) {
//        return bookService.findBooksByAuthorNameAndBookName(authorName, bookName);
//    }
}
