package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class CutParserTest {

	@Test
	public void testParseString() {
		final Parser p_012_cut_345 = Parsers.keyword("012").cut().then(Parsers.keyword("345"));
		final Parser p_012_345 = Parsers.keyword("012").then(Parsers.keyword("345"));
		
		// no cut 
		final Result r_012345 = p_012_345.parse("012345_");
		final Result r_0123X5 = p_012_345.parse("0123X5_");

		assertThat(r_012345.isSuccessful(), is(true));
		assertThat(r_012345.canBacktrack(), is(true));
		assertThat(r_0123X5.isSuccessful(), is(false));
		assertThat(r_0123X5.canBacktrack(), is(true));

		// cut
		final Result r_cut_012345 = p_012_cut_345.parse("012345_");
		final Result r_cut_0123X5 = p_012_cut_345.parse("0123X5_");
		final Result r_cut_0X2345 = p_012_cut_345.parse("0X2345_");
		
		assertThat(r_cut_012345.isSuccessful(), is(true));
		assertThat(r_cut_012345.canBacktrack(), is(false));
		assertThat(r_cut_0123X5.isSuccessful(), is(false));
		assertThat(r_cut_0123X5.canBacktrack(), is(false));
		assertThat(r_cut_0X2345.isSuccessful(), is(false));
		assertThat(r_cut_0X2345.canBacktrack(), is(true));
	}

}
