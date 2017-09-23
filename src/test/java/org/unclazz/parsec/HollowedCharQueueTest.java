package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

public class HollowedCharQueueTest {

	@Test
	public void testClear() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		q_one.push('1');
		final HollowedCharQueue q_two = new HollowedCharQueue();
		q_two.push('1');
		q_two.push('2');
		
		// Act
		q_empty.clear();
		q_one.clear();
		q_two.clear();
		
		// Assert
		assertTrue(q_empty.isEmpty());
		assertTrue(q_one.isEmpty());
		assertTrue(q_two.isEmpty());
	}

	@Test
	public void testPush() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		final HollowedCharQueue q_two = new HollowedCharQueue();
		
		// Act
		q_one.push('1');
		q_two.push('1');
		q_two.push('2');
		
		// Assert
		assertTrue(q_empty.size() == 0);
		assertTrue(q_one.size() == 1);
		assertTrue(q_two.size() == 2);
	}

	@Test
	public void testPeekFirst() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		final HollowedCharQueue q_two = new HollowedCharQueue();
		q_one.push('1');
		q_two.push('1');
		q_two.push('2');
		
		// Act
		// Assert
		try {
			q_empty.peekFirst();
			fail();
		}catch (NoSuchElementException e) {
			// OK
		}
		assertTrue(q_one.peekFirst() == '1');
		assertTrue(q_two.peekFirst() == '1');
	}

	@Test
	public void testPeekLast() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		final HollowedCharQueue q_two = new HollowedCharQueue();
		q_one.push('1');
		q_two.push('1');
		q_two.push('2');
		
		// Act
		// Assert
		try {
			q_empty.peekLast();
			fail();
		}catch (NoSuchElementException e) {
			// OK
		}
		assertTrue(q_one.peekLast() == '1');
		assertTrue(q_two.peekLast() == '2');
	}

	@Test
	public void testSize() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		final HollowedCharQueue q_two = new HollowedCharQueue();
		q_one.push('1');
		q_two.push('1');
		q_two.push('2');
		
		// Act
		// Assert
		assertTrue(q_empty.size() == 0);
		assertTrue(q_one.size() == 1);
		assertTrue(q_two.size() == 2);
	}

	@Test
	public void testIsEmpty() {
		// Arrange
		final HollowedCharQueue q_empty = new HollowedCharQueue();
		final HollowedCharQueue q_one = new HollowedCharQueue();
		final HollowedCharQueue q_two = new HollowedCharQueue();
		q_one.push('1');
		q_two.push('1');
		q_two.push('2');
		
		// Act
		// Assert
		assertTrue(q_empty.isEmpty());
		assertFalse(q_one.isEmpty());
		assertFalse(q_two.isEmpty());
	}

}
