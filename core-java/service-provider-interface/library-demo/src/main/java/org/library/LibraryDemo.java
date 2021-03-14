package org.library;

import org.library.spi.Book;

public class LibraryDemo {
    public static void main(String[] args) {
        LibraryService libraryService = LibraryService.getInstance();
        manageBookRequest("Clean Code", libraryService);
        manageBookRequest("The Lord of the Rings", libraryService);
    }

    private static void manageBookRequest(String bookName, LibraryService library) {
        Book book = library.getBook(bookName);
        if (book == null) {
            System.out.println("The library doesn't have the book '" + bookName + "' that you need.");
        } else {
            System.out.println("The book '" + bookName + "'was found, here are the details:" + book);
        }
    }
}
