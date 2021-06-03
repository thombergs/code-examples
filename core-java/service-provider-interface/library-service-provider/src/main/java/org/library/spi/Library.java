package org.library.spi;

public interface Library {
    String getCategory();
    Book getBook(String name);
}
