package io.pratik;

import java.util.List;

public class ParallelStreamingApp {

    public static void main(String[] args){
        ParallelStreamingApp parallelStreamingApp = new ParallelStreamingApp();
        parallelStreamingApp.processParallelStream();
    }

    public void processParallelStream(){
        List<String> list = List.of("washing machine", "Television", "Laptop", "grocery");
        list.parallelStream().forEach(System.out::println);
        
        list.parallelStream().forEachOrdered(System.out::println);


    }
    
}
