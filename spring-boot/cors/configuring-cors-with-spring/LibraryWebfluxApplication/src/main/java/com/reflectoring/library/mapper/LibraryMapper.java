package com.reflectoring.library.mapper;

import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.model.persistence.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    BookDto bookToBookDto(Book book);

    List<BookDto> bookToBookDto(List<Book> book);

    Book bookDtoToBook(BookDto bookDto);
}
