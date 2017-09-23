package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExactCharCharClassTest {

	@Test
	public void testContains() {
		// Arrange
		final CharClass c = new ExactCharCharClass('1');
		
		// Act
		// Assert
		assertFalse(c.contains('0'));
		assertTrue(c.contains('1'));
		assertFalse(c.contains('2'));
	}

	@Test
	public void testUnion() {
		// Arrange
		final CharClass c1 = new ExactCharCharClass('1');
		final CharClass c2 = new ExactCharCharClass('2');
		
		// Act
		final CharClass c3 = c1.union(c2);
		
		// Assert
		assertFalse(c3.contains('0'));
		assertTrue(c3.contains('1'));
		assertTrue(c3.contains('2'));
		assertFalse(c3.contains('3'));
	}

	@Test
	public void testPlus() {
		// Arrange
		final CharClass c1 = new ExactCharCharClass('1');
		
		// Act
		final CharClass c2 = c1.plus('3');
		
		// Assert
		assertFalse(c2.contains('0'));
		assertTrue(c2.contains('1'));
		assertFalse(c2.contains('2'));
		assertTrue(c2.contains('3'));
	}

}
