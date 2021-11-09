package com.reflectoring.io.java9;

public class DiamondOperator {
    public static void main(String[] args) {
        AppendingString<String> appending = new AppendingString<>() {
            @Override
            public String append(String a, String b) {
                return new StringBuilder(a).append("-").append(b).toString();
            }
        };

        String result = appending.append("Reflectoring", "Blog");
        System.out.println(result);
    }

    public abstract static class AppendingString<T>{
        public abstract T append(String a, String b);
    }
}
