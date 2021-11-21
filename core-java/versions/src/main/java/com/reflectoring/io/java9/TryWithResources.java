package com.reflectoring.io.java9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class TryWithResources {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(
                new StringReader("Hello world example!"));
        try {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final BufferedReader br2 = new BufferedReader(
                new StringReader("Hello world example2!"));
        try (br2) {
            System.out.println(br2.readLine());
        } catch (IOException e) {
            System.out.println("Error happened!");
        }

        final BufferedReader br3 = new BufferedReader(
                new StringReader("Hello world example3!"));
        try (BufferedReader reader = br3) {
            System.out.println(reader.readLine());
        } catch (IOException e) {
            System.out.println("Error happened!");
        }
    }
}
