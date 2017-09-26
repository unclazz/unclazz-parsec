package org.unclazz.parsec;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class KeywordParserTest {

	@Test
	public void testParseTextReader() {
		try {
			Parsers.keyword(null);
			fail();
		}catch (NullPointerException e) {
			// OK
		}
		try {
			Parsers.keyword("");
			fail();
		}catch (IllegalArgumentException e) {
			// OK
		}
		
		final Parser p_0123 = Parsers.keyword("0123");
		final Parser p_0123_1 = Parsers.keyword("0123", 1);
		final Parser p_0123_2 = Parsers.keyword("0123", 2);
		
		assertTrue(p_0123.parse("01234").isSuccessful());
		assertTrue(p_0123_1.parse("01234").isSuccessful());
		assertTrue(p_0123_2.parse("01234").isSuccessful());
		
		assertFalse(p_0123.parse("012_4").isSuccessful());
		assertFalse(p_0123_1.parse("012_4").isSuccessful());
		assertFalse(p_0123_2.parse("012_4").isSuccessful());
		
		assertTrue(p_0123_1.parse("_1234").canBacktrack());
		assertTrue(p_0123_2.parse("0_234").canBacktrack());
		
		assertFalse(p_0123_1.parse("0_234").canBacktrack());
		assertFalse(p_0123_2.parse("01_34").canBacktrack());
	}

}
