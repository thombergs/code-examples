package com.reflectoring;

public class DeprecatedDemo {

    @Deprecated(since = "4.5", forRemoval = true)
    public void testLegacyFunction() {

        System.out.println("This is a legacy function");
    }
}
