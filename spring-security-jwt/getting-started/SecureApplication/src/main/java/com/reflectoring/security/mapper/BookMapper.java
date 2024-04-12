package com.reflectoring.security.mapper;

import com.reflectoring.security.mapstruct.AuthorDto;
import com.reflectoring.security.mapstruct.BookDto;
import com.reflectoring.security.persistence.Author;
import com.reflectoring.security.persistence.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto bookToBookDto(Book book);

    List<BookDto> bookToBookDto(List<Book> book);

    AuthorDto authorToAuthorDto(Author author);

    Book bookDtoToBook(BookDto bookDto);

    Author authorDtoToAuthor(AuthorDto authorDto);
}

