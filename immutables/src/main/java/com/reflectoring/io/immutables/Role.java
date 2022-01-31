package com.reflectoring.io.immutables;

public enum Role {
    WRITER("writer"),
    READER("reader");

    public final String value;

    Role(String value){
        this.value = value;
    }

}
