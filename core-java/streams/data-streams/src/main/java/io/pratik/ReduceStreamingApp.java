package io.pratik;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.logging.Logger;

public class ReduceStreamingApp {
    private final Logger logger = Logger.getLogger(ReduceStreamingApp.class.getName());

    public static void main(String[] args){
        ReduceStreamingApp reduceStreamingApp = new ReduceStreamingApp();
        reduceStreamingApp.sumElements();
        reduceStreamingApp.joinString(",");
        reduceStreamingApp.aggregateElements();
    }

    public void sumElements(){
        int[] numbers = {5, 2, 8, 4,55, 9};
        int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b);
        int sumWithMethodRef = Arrays.stream(numbers).reduce(0, Integer::sum); 

        logger.info(sum + " " + sumWithMethodRef);
    }

    public void joinString(final String separator){
        String[] strings = {"a", "b", "c", "d", "e"};

        String joined = Arrays.stream(strings).reduce("", (a, b) -> {
            return !"".equals(a)?  a + separator + b : b;
           });

        // a|b|c|d|e , better uses the Java 8 String.join :)
        //String joined = String.join(separator, strings);

        logger.info(joined);
    }

    public void aggregateElements(){
        int[] numbers = {5, 2, 8, 4,55, 9};
        int sum = Arrays.stream(numbers).sum();
        OptionalInt max = Arrays.stream(numbers).max();
        OptionalInt min = Arrays.stream(numbers).max();
        long count = Arrays.stream(numbers).count();

        OptionalDouble average  = Arrays.stream(numbers).average();

        logger.info("sum:" + sum);
        logger.info("max:" + max.getAsInt());
        logger.info("min:" + min.getAsInt());
        logger.info("count:" + count);
        logger.info("average:" + average.getAsDouble());
    }
}
