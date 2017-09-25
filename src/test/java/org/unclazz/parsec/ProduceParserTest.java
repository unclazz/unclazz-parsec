package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;

public class ProduceParserTest extends ValParser<String> {

	@Test
	public final void testParseTextReader() throws IOException {
		final TextReader r = TextReader.from("0123__");
		final ValParser<String> p = this;
		final ValResult<String> res = p.parse(r);
		assertTrue(res.isSuccessful());
		assertTrue(res.value().equals("hello"));
		assertThat(res.start().index(), is(0));
		assertThat(res.end().index(), is(0));
	}

	@Override
	protected ValResultCore<String> doParse(Context ctx) throws IOException {
		return produce("hello").parse(ctx);
	}

}
