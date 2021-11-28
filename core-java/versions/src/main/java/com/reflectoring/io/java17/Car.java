package com.reflectoring.io.java17;

public final class Car extends Vehicle {
    Long kilomenters;
    Long year;

    public Car(String code, String engineType, Long kilomenters, Long year) {
        super(code, engineType);
        this.kilomenters = kilomenters;
        this.year = year;
    }
}
