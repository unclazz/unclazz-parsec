package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;

public class NotParserTest {

	@Test
	public void testParseString() {
		final Parser p_not = Parsers.not(Parsers.keyword("012"));
		final Parser p_val_not = Parsers.not(Parsers.keyword("012").val());
		
		final Result r_012 = p_not.parse("012_");
		final Result r_01X = p_not.parse("01X_");
		
		assertFalse(r_012.isSuccessful());
		assertTrue(r_012.end().index() == 3);
		
		assertTrue(r_01X.isSuccessful());
		assertTrue(r_01X.end().index() == 3);
		
		final Result r_val_012 = p_val_not.parse("012_");
		final Result r_val_01X = p_val_not.parse("01X_");
		
		assertFalse(r_val_012.isSuccessful());
		assertTrue(r_val_012.end().index() == 3);
		
		assertTrue(r_val_01X.isSuccessful());
		assertTrue(r_val_01X.end().index() == 3);
	}

}
