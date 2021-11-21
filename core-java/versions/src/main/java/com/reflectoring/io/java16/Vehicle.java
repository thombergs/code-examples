package com.reflectoring.io.java16;

import java.util.Objects;

public class Vehicle {
    String code;
    String engineType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Vehicle(String code, String engineType) {
        this.code = code;
        this.engineType = engineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return code.equals(vehicle.code) &&
                engineType.equals(vehicle.engineType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, engineType);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "code='" + code + '\'' +
                ", engineType='" + engineType + '\'' +
                '}';
    }
}
