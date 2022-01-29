package com.reflectoring;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SuppressWarningsDemo {

    public static void main(String[] args) {

        SuppressWarningsDemo swDemo = new SuppressWarningsDemo();
        swDemo.testSuppressWarning();
    }

    public void testSuppressWarning() {

        Map testMap = new HashMap();
        testMap.put(1, "Item_1");
        testMap.put(2, "Item_2");
        testMap.put(3, "Item_3");
    }
}
