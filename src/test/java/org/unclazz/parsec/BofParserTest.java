package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class BofParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		final TextReader r = TextReader.from("abc");
		final BofParser p = new BofParser();
		
		assertTrue(p.parse(r).isSuccessful());
		r.read(); // a[b]c
		assertFalse(p.parse(r).isSuccessful());
		r.read(); // ab[c]
		assertFalse(p.parse(r).isSuccessful());
		r.read(); // abc[ ]
		assertFalse(p.parse(r).isSuccessful());
	}

}
