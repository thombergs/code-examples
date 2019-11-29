package io.reflectoring.staticdata;

import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
class Quote {

    private final String text;
    private final String author;

    Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }

    String getText() {
        return text;
    }

    String getAuthor() {
        return author;
    }

}
