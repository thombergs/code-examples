package com.reflectoring;

@FunctionalInterface
interface Print {
    void printString(String testString);
}

public class FunctionalInterfaceTest {

    public static void main(String args[]) {

        Print testPrint = (String testString) -> System.out.println(testString);
        testPrint.printString("This is a String");
    }
}
