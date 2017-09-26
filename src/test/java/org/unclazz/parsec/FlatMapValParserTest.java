package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class FlatMapValParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		final ValParser<String> p1 = Parsers.keyword("hello").val();
		final ValParser<String> p2 = p1.flatMap(a-> Parsers.keyword(a.toUpperCase()).val());
		
		final TextReader tr0 = TextReader.from("helloHELLO_");
		final TextReader tr1 = TextReader.from("hellx_");
		final TextReader tr2 = TextReader.from("helloHELLX_");
		
		final ValResult<String> r0 = p2.parse(tr0);
		final ValResult<String> r1 = p2.parse(tr1);
		final ValResult<String> r2 = p2.parse(tr2);
		
		assertThat(r0.isSuccessful(), is(true));
		assertThat(r0.value(), is("HELLO"));
		assertThat(r0.end().index(), is(10));
		assertThat(r1.isSuccessful(), is(false));
		assertThat(r2.isSuccessful(), is(false));
	}

}
