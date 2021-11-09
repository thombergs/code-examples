package com.reflectoring.io.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodReference {

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();

        List<String> withoutMethodReference = cars.stream().map(car -> car.toString()).collect(Collectors.toList());
        System.out.println(withoutMethodReference);

        List<String> methodReference = cars.stream().map(Car::toString).collect(Collectors.toList());
        System.out.println(methodReference);


    }

    public class Car{
        public String model;
        public double kilometers;

        public Car(String model, double kilometers) {
            this.model = model;
            this.kilometers = kilometers;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "model='" + model + '\'' +
                    ", kilometers=" + kilometers +
                    '}';
        }
    }
}
