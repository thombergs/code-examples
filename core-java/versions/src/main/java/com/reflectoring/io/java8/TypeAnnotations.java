package com.reflectoring.io.java8;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class TypeAnnotations {

    public static void main(String[] args) {

        @NotNull String userName = args[0];

        List<String> request =
                new @NotEmpty ArrayList<>(Arrays.stream(args).collect(
                        Collectors.toList()));

        List<@Email String> emails;


    }

    @Target(value = {TYPE_USE})
    @Retention(value = RUNTIME)
    public @interface NotNull {
    }

    @Target(value = {TYPE_USE})
    @Retention(value = RUNTIME)
    public @interface NotEmpty {
    }

    @Target(value = {TYPE_USE})
    @Retention(value = RUNTIME)
    public @interface Email {
    }
}
