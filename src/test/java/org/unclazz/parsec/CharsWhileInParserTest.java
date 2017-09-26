package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class CharsWhileInParserTest {

	@Test
	public void testParseString() {
		final Parser p_ab_0 = Parsers.charsWhileIn("ab", 0);
		final Parser p_ab_1 = Parsers.charsWhileIn("ab", 1);
		
		final Result p_ab_0_r_0ab = p_ab_0.parse("___");
		final Result p_ab_0_r_1ab = p_ab_0.parse("a___");
		final Result p_ab_0_r_2ab = p_ab_0.parse("ab___");
		final Result p_ab_1_r_0ab = p_ab_1.parse("___");
		final Result p_ab_1_r_1ab = p_ab_1.parse("b___");
		final Result p_ab_1_r_2ab = p_ab_1.parse("ba___");
		
		assertThat(p_ab_0_r_0ab.isSuccessful(), is(true));
		assertThat(p_ab_0_r_0ab.end().index(), is(0));
		assertThat(p_ab_0_r_1ab.isSuccessful(), is(true));
		assertThat(p_ab_0_r_1ab.end().index(), is(1));
		assertThat(p_ab_0_r_2ab.isSuccessful(), is(true));
		assertThat(p_ab_0_r_2ab.end().index(), is(2));
		
		assertThat(p_ab_1_r_0ab.isSuccessful(), is(false));
		assertThat(p_ab_1_r_0ab.end().index(), is(0));
		assertThat(p_ab_1_r_1ab.isSuccessful(), is(true));
		assertThat(p_ab_1_r_1ab.end().index(), is(1));
		assertThat(p_ab_1_r_2ab.isSuccessful(), is(true));
		assertThat(p_ab_1_r_2ab.end().index(), is(2));
	}

}
