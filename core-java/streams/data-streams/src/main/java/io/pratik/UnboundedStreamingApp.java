package io.pratik;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnboundedStreamingApp {
    private final Logger logger = Logger.getLogger(UnboundedStreamingApp.class.getName());

    public static void main(String[] args) {
		UnboundedStreamingApp unboundedStreamingApp = new UnboundedStreamingApp();
        unboundedStreamingApp.generateStreamingData();
        unboundedStreamingApp.iterateStreamingData();
    } 

    public void generateStreamingData(){
        Stream.generate(()->UUID.randomUUID().toString())
        .limit(10)
        .forEach(logger::info);
    }

    public void iterateStreamingData(){
        Stream<Double> evenNumStream = Stream.iterate(2.0, element -> Math.pow(element, 2.0));

        List<Double> collect = evenNumStream
        .limit(5)
        .collect(Collectors.toList());

        collect.forEach(element->logger.info("value=="+element));
    }
}
