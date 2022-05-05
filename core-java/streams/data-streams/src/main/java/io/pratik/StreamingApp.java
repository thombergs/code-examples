package io.pratik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;


public class StreamingApp {

	private final Logger logger = Logger.getLogger(StreamingApp.class.getName());

	public static void main(String[] args) {
		StreamingApp streamingApp = new StreamingApp();
		/*streamingApp.createStreamFromArray();
		streamingApp.createStreamFromCollection();
		streamingApp.processStream();
		streamingApp.collectFromStream();
		streamingApp.mapStream();*/
		streamingApp.flatmapStream();
		//streamingApp.findFromStream();
		//streamingApp.createFromNullable();
		streamingApp.readFromFile("/Users/pratikdas/eclipse-workspace/StreamingApp/src/main/resources/sample.txt");
	}
	
	public void streamTypes() {
		
		Stream<Integer> stream = Stream.of(3, 4, 6, 2);
		
		IntStream integerStream = IntStream.of(3, 4, 6, 2);
		
		LongStream longStream = LongStream.of(3l, 4l, 6l, 2l);

		DoubleStream doubleStream = DoubleStream.of(3.0, 4.5, 6.7, 2.3);		
	}
	
	public void createStreamFromArray() {
		double[] elements = {3.0, 4.5, 6.7, 2.3};
		
		DoubleStream stream = Arrays.stream(elements);
		
		stream.forEach(e->System.out.println(e));
	}
	
	public void createStreamFromCollection() {
		Double[] elements = {3.0, 4.5, 6.7, 2.3};
		
		List<Double> elementsInCollection = Arrays.asList(elements);
		
		Stream<Double> stream = elementsInCollection.stream();
		
		Stream<Double> parallelStream = elementsInCollection.parallelStream();
		
		stream.forEach(e->System.out.println(e));
		
		parallelStream.forEach(e->System.out.println(e));
	}
	
	public void createStreams() {
		Stream<Integer> stream = Stream.of(3, 4, 6, 2);
		
		IntStream integerStream = IntStream.of(3, 4, 6, 2);
		
		LongStream longStream = LongStream.of(3l, 4l, 6l, 2l);

		DoubleStream doubleStream = DoubleStream.of(3.0, 4.5, 6.7, 2.3);		
	}

	public void readFromFile(final String filePath) {
		try (Stream<String> lines = Files.lines(Paths.get(filePath));){
			lines.forEach(logger::info);
		} catch (IOException e) {
			logger.info("i/o error " + e);
		}
	}
	
	public void processStream() {
		Double[] elements = {3.0, 4.5, 6.7, 2.3};
		
		Stream<Double> stream = Stream.of(elements);
		
		stream
		.map(e->e.intValue())
		.filter(e->e >3 )
		.forEach(e->System.out.println(e));
		          
	}
	
	public void processStreamWithFlatMap() {
		Double[] elements = {3.0, 4.5, 6.7, 2.3};
		
		Stream<Double> stream = Stream.of(elements);
		
	    
		          
	}

	public void collectFromStream() {
	
		List<String> productCategories = Stream.of("washing machine", "Television", "Laptop", "grocery", "essentials")
		                                    .collect(Collectors.toList());

		productCategories.forEach(logger::info);							
		
	
		          
	}

	public void printStream() {
		Stream<String> productCategories = Stream.of("washing machine", "Television", "Laptop", "grocery", "essentials");
	
		productCategories.forEach(logger::info);   
	}

	public void mapStream() {
		Stream<String> productCategories = Stream.of("washing machine", "Television", "Laptop", "grocery", "essentials");
	
		List<String> categoryCodes = productCategories.map(element->{
			 String code = null;
			 switch (element) {
				case "washing machine" : code = "1";break;
				case "Television" : code = "2";break;
				case "Laptop" : code = "3";break;
				case "grocery" : code = "4";break;
				case "essentials" : code = "5";break;
				case "default" : code = "6";
			} 
			return code;}).collect(Collectors.toList());

			categoryCodes.forEach(logger::info);	
    }

	public void flatmapStream() {
		List<List<String>> productByCategories = Arrays.asList( 
			Arrays.asList("washing machine", "Television"), 
			Arrays.asList("Laptop", "Camera", "Watch"), 
			Arrays.asList("grocery", "essentials"));

		List<String> products = productByCategories.stream().flatMap(Collection::stream
		).collect(Collectors.toList());
        
		logger.info("flattened elements::"+products);
		
    }


	public void findFromStream() {
		Stream<String> productCategories = Stream.of("washing machine", "Television", "Laptop", "grocery", "essentials");

		Optional<String> category = productCategories.findFirst();

		if(category.isPresent()) logger.info(category.get());
    }

	public void createFromNullable() {
		Stream<String> productCategories = Stream.ofNullable(null);

		long count = productCategories.count();

		logger.info("size=="+count);
    }

	

}
