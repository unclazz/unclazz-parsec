package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;

public class LookaheadParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		// Arrange
		final Parser parser = new ExactCharParser('a')
				.then(new LookaheadParser(new ExactCharParser('b')));
		final TextReader reader = TextReader.from("abc");
		
		// Act
		final Result result = parser.parse(reader);
		
		// Assert
		assertTrue(result.isSuccessful());
		assertThat(result.end().index(), is(1));
		assertThat(reader.position().index(), is(1));
		
	}

}
