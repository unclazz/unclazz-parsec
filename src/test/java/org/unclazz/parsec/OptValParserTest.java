package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class OptValParserTest {

	@Test
	public final void testOptValParser() {
		try {
			new OptValParser<String>(null);
			fail();
		} catch (NullPointerException e) {
			// OK
		}
	}

	@Test
	public final void testParseTextReader() throws IOException {
		// Arrange
		final ValParser<Optional<String>> p = new KeywordParser("hello").val().opt();
		final ValParser<Optional<String>> p2 = new KeywordParser("xhello").val().opt();
		final TextReader tr = TextReader.from("hello__");
		final TextReader tr2 = TextReader.from("hello__");
		
		// Act
		final ValResult<Optional<String>> res = p.parse(tr);
		final ValResult<Optional<String>> res2 = p2.parse(tr2);
		
		// Assert
		assertThat(res.isSuccessful(), is(true));
		assertThat(res.value().isPresent(), is(true));
		assertThat(res.value().get(), is("hello"));
		assertThat(res.end().index(), is(5));
		assertThat(tr.position().index(), is(5));
		
		assertThat(res2.isSuccessful(), is(true));
		assertThat(res2.value().isPresent(), is(false));
		assertThat(res2.end().index(), is(0));
		assertThat(tr2.position().index(), is(0));
	}

}
