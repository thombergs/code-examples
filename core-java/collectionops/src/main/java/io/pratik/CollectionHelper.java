/**
 * 
 */
package io.pratik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author pratikdas
 *
 */
public class CollectionHelper {
	
	public <T> List<T>[] split(List<T> listToSplit){
	    // determine the endpoints to use in `list.subList()` method
	    int[] endpoints = {0, (listToSplit.size() + 1)/2, listToSplit.size()};
	 
	    List<List<T>> sublists =
	            IntStream.rangeClosed(0, 1)
	                    .mapToObj(i -> listToSplit.subList(endpoints[i], endpoints[i + 1]))
	                    .collect(Collectors.toList());
	 
	    // return an array containing both lists
	    return new List[] {sublists.get(0), sublists.get(1)};
	}
	
	public List<Integer> add(final List<Integer> collA, final List<Integer> collB){

		return Stream.concat(collA.stream(), 
				collB.stream())
		.collect(Collectors.toList());
		
		
	}	
	
	public List<Integer> addWithFilter(final List<Integer> collA, final List<Integer> collB){

		return Stream.concat(collA.stream(), 
				collB.stream())
				.filter(element -> element > 2 )
		.collect(Collectors.toList());
	}	
	
	public List<Integer> union(final List<Integer> collA, final List<Integer> collB){
		Set<Integer> set = new HashSet<>();
		set.addAll(collA);
		set.addAll(collB);
		
		return new ArrayList<>(set);
		
	}
	
	public List<Integer> intersection(final List<Integer> collA, final List<Integer> collB){
		List<Integer> intersectElements = collA.stream()
				.filter(collB :: contains)
				.collect(Collectors.toList());
		
		if(!intersectElements.isEmpty()) {
			return intersectElements;
		}else {
			return Collections.emptyList();
		}
		
	}
	
	public List<Integer> subtract(final List<Integer> collA, final List<Integer> collB){
		List<Integer> intersectElements = intersection(collA,collB);
		
		List<Integer> subtractedElements = collA.stream().filter(element->!intersectElements.contains(element)).collect(Collectors.toList());
		
		if(!subtractedElements.isEmpty()) {
			return subtractedElements;
		}else {
			return Collections.emptyList();
		}
		
	}

}
