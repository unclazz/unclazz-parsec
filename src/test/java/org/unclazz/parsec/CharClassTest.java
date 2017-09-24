package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharClassTest {

	@Test
	public final void testBetween() {
		final CharClass clz = CharClass.between('1', '3');
		assertFalse(clz.contains('0'));
		assertTrue(clz.contains('1'));
		assertTrue(clz.contains('2'));
		assertTrue(clz.contains('3'));
		assertFalse(clz.contains('4'));
	}

	@Test
	public final void testExact() {
		final CharClass clz = CharClass.exact('1');
		assertFalse(clz.contains('0'));
		assertTrue(clz.contains('1'));
		assertFalse(clz.contains('2'));
	}

	@Test
	public final void testAnyOfCharArray() {
		final CharClass clz = CharClass.anyOf('1', '3');
		assertFalse(clz.contains('0'));
		assertTrue(clz.contains('1'));
		assertFalse(clz.contains('2'));
		assertTrue(clz.contains('3'));
		assertFalse(clz.contains('4'));
	}

	@Test
	public final void testAnyOfString() {
		final CharClass clz = CharClass.anyOf("13");
		assertFalse(clz.contains('0'));
		assertTrue(clz.contains('1'));
		assertFalse(clz.contains('2'));
		assertTrue(clz.contains('3'));
		assertFalse(clz.contains('4'));
	}

	@Test
	public final void testNot() {
		final CharClass clz = CharClass.not(CharClass.anyOf("13"));
		assertTrue(clz.contains('0'));
		assertFalse(clz.contains('1'));
		assertTrue(clz.contains('2'));
		assertFalse(clz.contains('3'));
		assertTrue(clz.contains('4'));
	}

}
