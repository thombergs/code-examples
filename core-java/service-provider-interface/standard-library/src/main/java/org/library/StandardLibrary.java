package org.library;

import java.util.Map;
import java.util.TreeMap;

import org.library.spi.Book;
import org.library.spi.Library;

public class StandardLibrary implements Library {

    private final Map<String, Book> books;

    public StandardLibrary() {
        books = new TreeMap<>();
        Book nineteenEightyFour = new Book("Nineteen Eighty-Four", "George Orwell",
                "It was a bright cold day in April, and the clocks were striking thirteen.");
        Book theLordOfTheRings = new Book("The Lord of the Rings", "J. R. R. Tolkien",
                "When Mr. Bilbo Baggins of Bag End announced that he would shortly be celebrating his " +
                        "eleventy-first birthday with a party of special magnificence, there was much talk and excitement in Hobbiton.");

        books.put("Nineteen Eighty-Four", nineteenEightyFour);
        books.put("The Lord of the Rings", theLordOfTheRings);
    }

    @Override
    public Book getBook(String name) {
        return books.get(name);
    }

}

