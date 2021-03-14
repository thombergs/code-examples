package org.library;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.library.spi.Book;
import org.library.spi.Library;

public class LibraryService {

    private static LibraryService libraryService;
    private final ServiceLoader<Library> loader;

    private LibraryService() {
        loader = ServiceLoader.load(Library.class);
    }

    public static synchronized LibraryService getInstance() {
        if (libraryService == null) {
            libraryService = new LibraryService();
        }
        return libraryService;
    }

    public Book getBook(String name) {
        Book book = null;
        Iterator<Library> libraries = loader.iterator();
        while (book == null && libraries.hasNext()) {
            Library library = libraries.next();
            book = library.getBook(name);
        }
        return book;
    }
}
