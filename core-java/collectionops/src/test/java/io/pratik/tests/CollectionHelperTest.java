/**
 * 
 */
package io.pratik.tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.pratik.CollectionHelper;

/**
 * @author pratikdas
 *
 */
class CollectionHelperTest {
	
	private CollectionHelper collectionHelper;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		collectionHelper = new CollectionHelper();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testUnion() {
		List<Integer> union = collectionHelper.union(
				List.of(9, 8, 5, 4, 7), 
				List.of(1, 3, 99, 4, 7));
		
		
		Assertions.assertArrayEquals(
				List.of(9, 8, 5, 4, 7, 1, 3, 99).toArray(), 
				union.toArray());
		
	}
	
	@Test
	void testIntersection() {
		List<Integer> intersection = collectionHelper.intersection(
				List.of(9,8,5,4,7, 15, 15), 
				List.of(1,3,99,4,7));
		
		Assertions.assertArrayEquals(
				List.of(4,7).toArray(), 
				intersection.toArray());
	}
	
	@Test
	void testXOR() {
		List<Integer> xorList = collectionHelper.xor(
				List.of(9, 8,  5, 4, 7), 
				List.of(1, 99, 4, 7));
		
		Assertions.assertArrayEquals(
				List.of(9, 8, 5, 1, 99).toArray(), 
				xorList.toArray());
	}
	
	@Test
	void testNOT() {
		List<Integer> xorList = collectionHelper.not(
				List.of(9,8,5,4,7), 
				List.of(1,99,4,7));
		
		Assertions.assertArrayEquals(
				List.of(9,8,5).toArray(), 
				xorList.toArray());
	}
	
	@Test
	void testAddition() {
		List<Integer> sub = collectionHelper.add(
				List.of(9,8,5,4), 
				List.of(1,3,99,4,7));
		
		
		Assertions.assertArrayEquals(
				List.of(9,8,5,4,1,3,99,4,7).toArray(), 
				sub.toArray());
	}
	
	@Test
	void testAdditionWithFilter() {
		List<Integer> list = collectionHelper.addWithFilter(
				List.of(9,8,5,4), 
				List.of(1,3,99,4,7));
		
		
		Assertions.assertArrayEquals(
				List.of(9,8,5,4,3,99,4,7).toArray(), 
				list.toArray());
	}

	
	@Test
	void testSubtraction() {
		List<Integer> sub = collectionHelper.subtract(
				List.of(9,8,5,4,7, 15, 15), 
				List.of(1,3,99,4,7));
		
		
		Assertions.assertArrayEquals(
				List.of(9,8,5,15,15).toArray(), 
				sub.toArray());
	}
	
	@Test
	void testPartition() {
		Collection<List<Integer>> partitions = collectionHelper.partition(
				List.of(9, 8, 5, 4, 7, 15, 15), 2);
		
		Iterator<List<Integer>> iter = partitions.iterator();
		
		int loop = 0;
		while(iter.hasNext()) {
			List<Integer> element = iter.next();
			System.out.println(element);
			if(loop == 0)
			   assertArrayEquals(List.of(9, 8).toArray(),element.toArray());
			else if(loop == 1)
				   assertArrayEquals(List.of(5, 4).toArray(),element.toArray());
			else if(loop == 2)
				   assertArrayEquals(List.of(7, 15).toArray(),element.toArray());
			else if(loop == 3)
				   assertArrayEquals(List.of(15).toArray(),element.toArray());
		
			++loop;
		}
		
	
	}
	
	@Test
	void testRemoveDuplicates() {
		List<Integer> uniqueElements = collectionHelper.removeDuplicates(
				List.of(9, 8, 5, 4, 4, 7, 15, 15));
		
		
		
		Assertions.assertArrayEquals(
				List.of(9, 8, 5, 4, 7, 15).toArray(), 
				uniqueElements.toArray());
	}
	
	@Test
	void testSplit() {
		List<Integer>[] subLists = collectionHelper.split(
				List.of(9, 8, 5, 4, 7, 15, 15));
		
		
		Assertions.assertArrayEquals(
				List.of(9,8,5,4).toArray(), 
				subLists[0].toArray());
		
		Assertions.assertArrayEquals(
				List.of(7,15,15).toArray(), 
				subLists[1].toArray());
	}

}
