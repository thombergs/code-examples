package com.reflectoring.lombok.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Book {
    private String isbn;

    private String publication;

    private String title;

    private List<Author> authors;

    private final Genre genre = Genre.FICTION;
}
