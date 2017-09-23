package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class MeansValParserTest {

	@Test
	public void testParseString() {
		final Parser abc = new KeywordParser("abc");
		final ValParser<Integer> abcMeans123 = abc.means(123);
		final ValParser<Integer> abcMeans456 = abc.means(() -> 456);
		final ValResult<Integer> res1 = abcMeans123.parse("abc_");
		final ValResult<Integer> res2 = abcMeans456.parse("abc_");
		
		assertTrue(res1.isSuccessful());
		assertTrue(res1.value().equals(123));
		assertTrue(res2.isSuccessful());
		assertTrue(res2.value().equals(456));
	}

}
