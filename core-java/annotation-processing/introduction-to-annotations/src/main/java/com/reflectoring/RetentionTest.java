package com.reflectoring;

public class RetentionTest {

    public static void main(String[] args) {

        SourceRetention[] sourceRetention = new EmployeeRetentionAnnotation().getClass().getAnnotationsByType(SourceRetention.class);
        System.out.println("Source Retentions at run-time: " + sourceRetention.length);

        RuntimeRetention[] runtimeRetention = new EmployeeRetentionAnnotation().getClass().getAnnotationsByType(RuntimeRetention.class);
        System.out.println("Run-time Retentions at run-time: " + runtimeRetention.length);

        ClassRetention[] classRetention = new EmployeeRetentionAnnotation().getClass().getAnnotationsByType(ClassRetention.class);
        System.out.println("Class Retentions at run-time: " + classRetention.length);
    }
}
