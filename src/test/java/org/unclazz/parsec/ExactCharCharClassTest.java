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
		final CharClass clz_1 = new ExactCharCharClass('1');
		final CharClass clz_2 = new ExactCharCharClass('2');
		final CharClass clz_3 = new ExactCharCharClass('3');
		
		// Act
		final CharClass clz_1_union_2 = clz_1.union(clz_2);
		final CharClass clz_1_union_3 = clz_1.union(clz_3);
		final CharClass clz_1_union_rng_1_3 = clz_1.union(CharClass.between('1', '3')); // 包含
		final CharClass clz_1_union_rng_2_3 = clz_1.union(CharClass.between('2', '3')); // 隣接
		final CharClass clz_1_union_rng_3_4 = clz_1.union(CharClass.between('3', '4')); // 独立
		
		// Assert
		assertFalse(clz_1_union_2.contains('0'));
		assertTrue(clz_1_union_2.contains('1'));
		assertTrue(clz_1_union_2.contains('2'));
		assertFalse(clz_1_union_2.contains('3'));
		
		assertFalse(clz_1_union_3.contains('0'));
		assertTrue(clz_1_union_3.contains('1'));
		assertFalse(clz_1_union_3.contains('2'));
		assertTrue(clz_1_union_3.contains('3'));
		
		assertFalse(clz_1_union_rng_1_3.contains('0'));
		assertTrue(clz_1_union_rng_1_3.contains('1'));
		assertTrue(clz_1_union_rng_1_3.contains('2'));
		assertTrue(clz_1_union_rng_1_3.contains('3'));
		assertFalse(clz_1_union_rng_1_3.contains('4'));
		
		assertFalse(clz_1_union_rng_2_3.contains('0'));
		assertTrue(clz_1_union_rng_2_3.contains('1'));
		assertTrue(clz_1_union_rng_2_3.contains('2'));
		assertTrue(clz_1_union_rng_2_3.contains('3'));
		assertFalse(clz_1_union_rng_2_3.contains('4'));
		
		assertFalse(clz_1_union_rng_3_4.contains('0'));
		assertTrue(clz_1_union_rng_3_4.contains('1'));
		assertFalse(clz_1_union_rng_3_4.contains('2'));
		assertTrue(clz_1_union_rng_3_4.contains('3'));
		assertTrue(clz_1_union_rng_3_4.contains('4'));
	}

	@Test
	public void testPlus() {
		// Arrange
		final CharClass c1 = new ExactCharCharClass('1');
		
		// Act
		final CharClass c2 = c1.plus('3');
		final CharClass c3 = c1.plus('2');
		
		// Assert
		assertFalse(c2.contains('0'));
		assertTrue(c2.contains('1'));
		assertFalse(c2.contains('2'));
		assertTrue(c2.contains('3'));
		
		assertFalse(c3.contains('0'));
		assertTrue(c3.contains('1'));
		assertTrue(c3.contains('2'));
		assertFalse(c3.contains('3'));
	}

}
