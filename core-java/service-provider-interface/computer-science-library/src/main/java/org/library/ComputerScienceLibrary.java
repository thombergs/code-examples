package org.library;

import java.util.Map;
import java.util.TreeMap;

import org.library.spi.Book;
import org.library.spi.Library;

public class ComputerScienceLibrary implements Library {

    public static final String COMPUTER_SCIENCE_LIBRARY = "COMPUTER_SCIENCE";

    private final Map<String, Book> books;

    public ComputerScienceLibrary() {
        books = new TreeMap<>();

        Book cleanCode = new Book("Clean Code", "Robert C. Martin",
                "Even bad code can function. But if code isn’t clean, it can bring a development organization to its knees");
        Book pragmaticProgrammer = new Book("The Pragmatic Programmer", "Hunt Andrew, Thomas David",
                "This book is filled with both technical and professional practical advices for developers in order become better developers.");

        books.put(cleanCode.getName(), cleanCode);
        books.put(pragmaticProgrammer.getName(), pragmaticProgrammer);
    }

    @Override
    public String getCategory() {
        return COMPUTER_SCIENCE_LIBRARY;
    }

    @Override
    public Book getBook(String name) {
        return books.get(name);
    }
}
