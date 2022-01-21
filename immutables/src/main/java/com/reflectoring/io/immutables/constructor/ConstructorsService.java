package com.reflectoring.io.immutables.constructor;

public class ConstructorsService {
    public static void main(String[] args) {

    }

    // Out of the box constructor
    public static ConstructorArticle createConstructorArticle(){
        return ImmutableConstructorArticle.of(0,"Lorem ipsum article!", "Lorem ipsum...");
    }

    // How to use standard new constructor
    public static PlainPublicConstructorArticle createPlainPublicConstructorArticle(){
        return new ImmutablePlainPublicConstructorArticle(0,"Lorem ipsum","Lorem ipsum...");
    }
}
