package io.pratik;

import java.util.logging.Logger;
import java.util.stream.Stream;

public class StreamMatcherApp {
    private final Logger logger = Logger.getLogger(StreamMatcherApp.class.getName());
    public static void main(String[] args) {
        StreamMatcherApp streamMatcherApp = new StreamMatcherApp();
        streamMatcherApp.findAnyMatch();
        streamMatcherApp.findAllMatch();
        streamMatcherApp.findNoneMatch();
    }

    public void findAnyMatch(){
        Stream<String> productCategories = Stream.of("washing machine", "Television", "Laptop", "grocery", "essentials");
	    
        boolean isPresent = productCategories.anyMatch(e->e.equals("Laptop"));
        logger.info("isPresent::"+isPresent);

    }

    public void findAllMatch(){
        Stream<Integer> productCategories = Stream.of(4,5,7,9,10);
	    
        boolean allElementsMatch = productCategories.allMatch(e->e < 11);
        logger.info("allElementsMatch::"+allElementsMatch);

    }

    public void findNoneMatch(){
        Stream<Integer> productCategories = Stream.of(4,5,7,9,10);
	    
        boolean noElementsMatch = productCategories.noneMatch(e->e < 4);
        logger.info("noElementsMatch::"+noElementsMatch);

    }
    
}
