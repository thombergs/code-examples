package com.reflectoring.io.java14;

public class SwitchExpression {

    public static void main(String[] args) {
        int days = 0;
        Month month = Month.APRIL;

        switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER,
                    DECEMBER:
                days = 31;
                break;
            case FEBRUARY:
                days = 28;
                break;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER:
                days = 30;
                break;
            default:
                throw new IllegalStateException();
        }

        days = switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> 31;
            case FEBRUARY -> 28;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30;
            default -> throw new IllegalStateException();
        };

        days = switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> {
                System.out.println(month);
                yield 31;
            }
            case FEBRUARY -> {
                System.out.println(month);
                yield 28;
            }
            case APRIL, JUNE, SEPTEMBER, NOVEMBER -> {
                System.out.println(month);
                yield 30;
            }
            default -> throw new IllegalStateException();
        };

    }

    public enum Month {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER
    }
}


