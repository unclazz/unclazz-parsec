package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class CaptureParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		final ValParser<String> p_a_rep = Parsers.exact('a').rep().val();
		final ValParser<String> p_a_ba_rep = Parsers.exact('a').rep()
							.then(Parsers.keyword("ba").opt()).val();

		// Act
		final ValResult<String> r_a_rep_bbbbb = p_a_rep.parse("bbbbb");
		final ValResult<String> r_a_rep_aabbb = p_a_rep.parse("aabbb");
		final ValResult<String> r_a_rep_aabab = p_a_rep.parse("aabab");

		final ValResult<String> r_a_ba_rep_aabbb = p_a_ba_rep.parse("aabbb");
		final ValResult<String> r_a_ba_rep_aabab = p_a_ba_rep.parse("aabab");
		
		// Asseert
		assertThat(r_a_rep_bbbbb.isSuccessful(), is(true));
		assertThat(r_a_rep_bbbbb.value(), is(""));
		
		assertThat(r_a_rep_aabbb.isSuccessful(), is(true));
		assertThat(r_a_rep_aabbb.value(), is("aa"));
		
		assertThat(r_a_rep_aabab.isSuccessful(), is(true));
		assertThat(r_a_rep_aabab.value(), is("aa"));
		
		assertThat(r_a_ba_rep_aabbb.isSuccessful(), is(true));
		assertThat(r_a_ba_rep_aabbb.value(), is("aa"));
		
		assertThat(r_a_ba_rep_aabab.isSuccessful(), is(true));
		assertThat(r_a_ba_rep_aabab.value(), is("aaba"));
	}

}
