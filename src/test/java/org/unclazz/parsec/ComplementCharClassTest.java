package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplementCharClassTest {

	@Test
	public void testContains() {
		assertTrue(CharClass.not(CharClass.exact('a')).contains('b'));
		assertFalse(CharClass.not(CharClass.exact('a')).contains('a'));
	}

}
