package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;

public class OptParserTest {

	@Test
	public final void testOptParser() {
		try {
			new OptParser(null);
			fail();
		} catch (NullPointerException e) {
			// OK
		}
	}

	@Test
	public final void testParseTextReader() throws IOException {
		// Arrange
		final Parser p = new KeywordParser("hello").opt();
		final Parser p2 = new KeywordParser("xhello").opt();
		final TextReader tr = TextReader.from("hello__");
		final TextReader tr2 = TextReader.from("hello__");
		
		// Act
		final Result res = p.parse(tr);
		final Result res2 = p2.parse(tr2);
		
		// Assert
		assertThat(res.isSuccessful(), is(true));
		assertThat(res.end().index(), is(5));
		assertThat(tr.position().index(), is(5));
		
		assertThat(res2.isSuccessful(), is(true));
		assertThat(res2.end().index(), is(0));
		assertThat(tr2.position().index(), is(0));
	}

}
