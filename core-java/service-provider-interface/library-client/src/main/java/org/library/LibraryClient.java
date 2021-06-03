package org.library;

public class LibraryClient {
    public static void main(String[] args) {
        LibraryService libraryService = LibraryService.getInstance();
        requestBook("Clean Code", libraryService);
        requestBook("The Lord of the Rings", libraryService);
        requestBook("The Lord of the Rings", "COMPUTER_SCIENCE", libraryService);
    }

    private static void requestBook(String bookName, LibraryService library) {
        library.getBook(bookName)
                .ifPresentOrElse(book -> System.out.println("The book '" + bookName + "' was found, here are the details:" + book),
                        () -> System.out.println("The library doesn't have the book '" + bookName + "' that you need."));
    }

    private static void requestBook(String bookName,String category, LibraryService library) {
        library.getBook(bookName, category)
                .ifPresentOrElse(book -> System.out.println("The book '" + bookName + "' was found in  " + category + ", here are the details:" + book),
                        () -> System.out.println("The library " + category + " doesn't have the book '" + bookName + "' that you need."));
    }
}
