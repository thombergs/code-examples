package com.reflectoring.io.java16;

public class Car extends Vehicle{
    Long kilomenters;
    Long year;

    public Car(String code, String engineType, Long kilomenters, Long year) {
        super(code, engineType);
        this.kilomenters = kilomenters;
        this.year = year;
    }
}
