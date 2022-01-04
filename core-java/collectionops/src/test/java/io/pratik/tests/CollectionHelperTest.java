/**
 * 
 */
package io.pratik.tests;

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
				List.of(9,8,5,4,7), 
				List.of(1,3,99,4,7));
		
		
		Assertions.assertArrayEquals(
				List.of(1, 3, 99, 4, 5, 7, 8, 9).toArray(), 
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
	void testSplit() {
		List<Integer>[] subLists = collectionHelper.split(
				List.of(9,8,5,4,7, 15, 15));
		
		
		Assertions.assertArrayEquals(
				List.of(9,8,5,4).toArray(), 
				subLists[0].toArray());
		
		Assertions.assertArrayEquals(
				List.of(7,15,15).toArray(), 
				subLists[1].toArray());
	}

}
