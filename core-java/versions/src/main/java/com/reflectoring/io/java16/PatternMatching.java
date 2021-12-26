package com.reflectoring.io.java16;

import java.util.Calendar;

public class PatternMatching {
    public static void main(String[] args) {
        Car car = new Car("Kia", "Gas", 10000l, 2021l);
        Bicycle bicycle =
                new Bicycle("Greyp", "Electricity", "Mountain", 21l);

        System.out.println("Car:" + price(car));
        System.out.println("Bycicle: " + price(bicycle));
    }

    public static double price(Vehicle v) {
        if (v instanceof Car c) {
            return 10000 - c.kilomenters * 0.01 -
                    (Calendar.getInstance().get(Calendar.YEAR) -
                            c.year) * 100;
        } else if (v instanceof Bicycle b) {
            return 1000 + b.wheelSize * 10;
        } else throw new IllegalArgumentException();
    }

    public static double priceOld(Vehicle v) {
        if (v instanceof Car) {
            Car c = (Car) v;
            return 10000 - c.kilomenters * 0.01 -
                    (Calendar.getInstance().get(Calendar.YEAR) -
                            c.year) * 100;
        } else if (v instanceof Bicycle) {
            Bicycle b = (Bicycle) v;
            return 1000 + b.wheelSize * 10;
        } else throw new IllegalArgumentException();

    }
}
