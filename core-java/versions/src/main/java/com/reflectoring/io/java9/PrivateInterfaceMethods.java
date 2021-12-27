package com.reflectoring.io.java9;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PrivateInterfaceMethods {
    public static void main(String[] args) {
        TestingNames names = new TestingNames();
        System.out.println(names.fetchInitialData());
    }

    public static class TestingNames implements NamesInterface {
        public TestingNames() {
        }
    }

    public interface NamesInterface {
        default List<String> fetchInitialData() {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(this.getClass()
                            .getResourceAsStream("/names.txt")))) {
                return readNames(br);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private List<String> readNames(BufferedReader br)
                throws IOException {
            ArrayList<String> names = new ArrayList<>();
            String name;
            while ((name = br.readLine()) != null) {
                names.add(name);
            }
            return names;
        }
    }
}
