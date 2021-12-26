package com.reflectoring.io.java9;

public class DiamondOperator {

    StringAppender<String> appending = new StringAppender<>() {
        @Override
        public String append(String a, String b) {
            return new StringBuilder(a).append("-").append(b)
                    .toString();
        }
    };

    public abstract static class StringAppender<T> {
        public abstract T append(String a, String b);
    }
}
