package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class EofParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		final TextReader r = TextReader.from("abc");
		final EofParser p = new EofParser();
		
		assertFalse(p.parse(r).isSuccessful());
		r.read(); // a[b]c
		assertFalse(p.parse(r).isSuccessful());
		r.read(); // ab[c]
		assertFalse(p.parse(r).isSuccessful());
		r.read(); // abc[ ]
		assertTrue(p.parse(r).isSuccessful());
	}

}
