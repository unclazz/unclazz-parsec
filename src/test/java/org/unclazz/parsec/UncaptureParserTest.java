package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class UncaptureParserTest {

	@Test
	public void testParseTextReader() throws IOException {
		final Parser p_a_rep = Parsers.exact('a').rep().val().unval();
		final Parser p_a_ba_rep = Parsers.exact('a').rep()
							.then(Parsers.keyword("ba").opt()).val().unval();

		// Act
		final Result r_a_rep_bbbbb = p_a_rep.parse("bbbbb");
		final Result r_a_rep_aabbb = p_a_rep.parse("aabbb");
		final Result r_a_rep_aabab = p_a_rep.parse("aabab");

		final Result r_a_ba_rep_aabbb = p_a_ba_rep.parse("aabbb");
		final Result r_a_ba_rep_aabab = p_a_ba_rep.parse("aabab");
		
		// Asseert
		assertThat(r_a_rep_bbbbb.isSuccessful(), is(true));
		assertThat(r_a_rep_bbbbb.end().index(), is(0));
		
		assertThat(r_a_rep_aabbb.isSuccessful(), is(true));
		assertThat(r_a_rep_aabbb.end().index(), is(2));
		
		assertThat(r_a_rep_aabab.isSuccessful(), is(true));
		assertThat(r_a_rep_aabab.end().index(), is(2));
		
		assertThat(r_a_ba_rep_aabbb.isSuccessful(), is(true));
		assertThat(r_a_ba_rep_aabbb.end().index(), is(2));
		
		assertThat(r_a_ba_rep_aabab.isSuccessful(), is(true));
		assertThat(r_a_ba_rep_aabab.end().index(), is(4));
	}

}
