package com.reflectoring.library.mapper;

import com.reflectoring.library.model.mapstruct.AuthorDto;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.model.persistence.Author;
import com.reflectoring.library.model.persistence.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    BookDto bookToBookDto(Book book);

    List<BookDto> bookToBookDto(List<Book> book);

    AuthorDto authorToAuthorDto(Author author);

    Book bookDtoToBook(BookDto bookDto);

    Author authorDtoToAuthor(AuthorDto authorDto);
}
