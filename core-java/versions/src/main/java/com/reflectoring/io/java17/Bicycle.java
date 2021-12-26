package com.reflectoring.io.java17;

public final class Bicycle extends Vehicle {
    String type;
    Long wheelSize;

    public Bicycle(String code, String engineType, String type, Long wheelSize) {
        super(code, engineType);
        this.type = type;
        this.wheelSize = wheelSize;
    }
}
