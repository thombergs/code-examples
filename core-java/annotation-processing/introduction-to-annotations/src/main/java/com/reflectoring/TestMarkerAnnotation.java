package com.reflectoring;

public class TestMarkerAnnotation {

    public static void main(String[] args) {

        XYZClient client = new XYZClient();
        Class clientClass = client.getClass();

        if (clientClass.isAnnotationPresent(CSV.class)){
            System.out.println("Write client data to CSV.");
        } else {
            System.out.println("Write client data to Excel file.");
        }
    }
}
