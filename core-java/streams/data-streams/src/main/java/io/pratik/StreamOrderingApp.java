package io.pratik;

import java.util.stream.Stream;

public class StreamOrderingApp {
    public static void main(String[] args) {
        StreamOrderingApp streamOrderingApp = new StreamOrderingApp();

        streamOrderingApp.sortElements();
        streamOrderingApp.sortElementsWithComparator();
    }

    public void sortElements() {
        Stream<Integer> productCategories = Stream.of(4,15,8,7,9,10);
        Stream<Integer>  sortedStream = productCategories.sorted();
        sortedStream.forEach(System.out::println);
    }

    public void sortElementsWithComparator() {
        Stream<Integer> productCategories = Stream.of(4,15,8,7,9,10);
        Stream<Integer>  sortedStream = productCategories.sorted((o1, o2) -> o2 - o1);
        sortedStream.forEach(System.out::println);
    }
}
