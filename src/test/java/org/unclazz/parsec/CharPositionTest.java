package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharPositionTest {

	@Test
	public void testOfBof() {
		final CharPosition cp = CharPosition.ofBof();
		assertTrue(cp.column() == 1);
		assertTrue(cp.line() == 1);
		assertTrue(cp.index() == 0);
	}

	@Test
	public void testLine() {
		final CharPosition cp = CharPosition.ofBof();
		assertTrue(cp.line() == 1);
		final CharPosition cp2 = cp.nextLine();
		assertTrue(cp.line() == 1);
		assertTrue(cp2.line() == 2);
		assertTrue(cp2.column() == 1);
		assertTrue(cp2.index() == 1);
	}

	@Test
	public void testColumn() {
		final CharPosition cp = CharPosition.ofBof();
		assertTrue(cp.column() == 1);
		final CharPosition cp2 = cp.nextColumn();
		assertTrue(cp.column() == 1);
		assertTrue(cp2.line() == 1);
		assertTrue(cp2.column() == 2);
		assertTrue(cp2.index() == 1);
	}

	@Test
	public void testIndex() {
		final CharPosition cp = CharPosition.ofBof();
		final CharPosition cp2 = cp.nextColumn();
		final CharPosition cp3 = cp2.nextLine();
		
		assertTrue(cp.index() == 0);
		assertTrue(cp2.index() == 1);
		assertTrue(cp3.index() == 2);
	}

	@Test
	public void testNextColumn() {
		final CharPosition cp = CharPosition.ofBof();
		final CharPosition cp2 = cp.nextColumn();
		final CharPosition cp3 = cp2.nextColumn();
		
		assertTrue(cp.column() == 1);
		assertTrue(cp2.column() == 2);
		assertTrue(cp3.column() == 3);
	}

	@Test
	public void testNextLine() {
		final CharPosition cp = CharPosition.ofBof();
		final CharPosition cp2 = cp.nextLine();
		final CharPosition cp3 = cp2.nextLine();
		
		assertTrue(cp.line() == 1);
		assertTrue(cp2.line() == 2);
		assertTrue(cp3.line() == 3);
	}

}
