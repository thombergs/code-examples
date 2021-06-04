package org.library;

import java.util.Map;
import java.util.TreeMap;

import org.library.spi.Book;
import org.library.spi.Library;

public class ClassicsLibrary implements Library {

    public static final String CLASSICS_LIBRARY = "CLASSICS";
    private final Map<String, Book> books;

    public ClassicsLibrary() {
        books = new TreeMap<>();
        Book nineteenEightyFour = new Book("Nineteen Eighty-Four", "George Orwell",
                "It was a bright cold day in April, and the clocks were striking thirteen.");
        Book theLordOfTheRings = new Book("The Lord of the Rings", "J. R. R. Tolkien",
                "When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his " +
                        "eleventy-first birthday with a party of special magnificence, there was much talk and excitement in Hobbiton.");
        Book cleanCode = new Book("Clean Code", "Robert C. Martin",
                "ClassicsEven bad code can function. But if code isn’t clean, it can bring a development organization to its knees");

        books.put("Nineteen Eighty-Four", nineteenEightyFour);
        books.put("The Lord of the Rings", theLordOfTheRings);
        books.put("Clean Code", cleanCode);
    }

    @Override
    public String getCategory() {
        return CLASSICS_LIBRARY;
    }

    @Override
    public Book getBook(String name) {
        return books.get(name);
    }

}

