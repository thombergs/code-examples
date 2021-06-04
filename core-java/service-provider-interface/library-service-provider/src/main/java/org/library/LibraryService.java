package org.library;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
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

    public void refresh() {
        loader.reload();
    }

    public Optional<Book> getBook(String name) {
        Book book = null;
        Iterator<Library> libraries = loader.iterator();
        while (book == null && libraries.hasNext()) {
            Library library = libraries.next();
            book = library.getBook(name);
        }
        return Optional.ofNullable(book);
    }

    public Optional<Book> getBook(String name, String category) {
        return loader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(library -> library.getCategory().equals(category))
                .map(library -> library.getBook(name))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
