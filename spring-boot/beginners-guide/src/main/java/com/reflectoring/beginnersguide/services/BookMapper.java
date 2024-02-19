package com.reflectoring.beginnersguide.services;

import com.reflectoring.beginnersguide.controllers.dto.BookRequest;
import com.reflectoring.beginnersguide.controllers.dto.BookResponse;
import com.reflectoring.beginnersguide.domain.Book;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
class BookMapper {

    BookResponse map(Book book){
        return BookResponse.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .currentlyAvailableNumber(
                        book.getNumberOfInstances())
                .publishedOn(book.getPublication().toString())
                .build();
    }
    List<BookResponse> mapAll(List<Book> books){
        return books != null ? books.stream().map(this::map).collect(
                Collectors.toList()) : new ArrayList<>();
    }

    Book map(BookRequest request){
        return new Book(0L,request.getTitle(),
                request.getAuthor(),
                Date.valueOf(request.getPublishedOn()),
                (int) request.getCurrentlyAvailableNumber(),
                new ArrayList<>());
    }
}
