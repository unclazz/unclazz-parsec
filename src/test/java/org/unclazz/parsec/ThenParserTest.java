package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ThenParserTest {

	@Test
	public void testParseString() {
		final Parser p_0_1 = Parsers.exact('0').then(Parsers.exact('1'));
		final Parser p_0_cut_1 = Parsers.exact('0').cut().then(Parsers.exact('1'));
		
		final Result p_0_1_r_012 = p_0_1.parse("012");
		final Result p_0_cut_1_r_012 = p_0_cut_1.parse("012");
		
		assertThat(p_0_1_r_012.isSuccessful(), is(true));
		assertThat(p_0_1_r_012.canBacktrack(), is(true));
		assertThat(p_0_1_r_012.end().index(), is(2));
		assertThat(p_0_cut_1_r_012.isSuccessful(), is(true));
		assertThat(p_0_cut_1_r_012.canBacktrack(), is(false));
		assertThat(p_0_cut_1_r_012.end().index(), is(2));
		
		final Result p_0_1_r_X12 = p_0_1.parse("X12");
		final Result p_0_cut_1_r_X12 = p_0_cut_1.parse("X12");
		
		assertThat(p_0_1_r_X12.isSuccessful(), is(false));
		assertThat(p_0_1_r_X12.canBacktrack(), is(true));
		assertThat(p_0_1_r_X12.end().index(), is(0));
		assertThat(p_0_cut_1_r_X12.isSuccessful(), is(false));
		assertThat(p_0_cut_1_r_X12.canBacktrack(), is(true));
		assertThat(p_0_cut_1_r_X12.end().index(), is(0));
		
		final Result p_0_1_r_0X2 = p_0_1.parse("0X2");
		final Result p_0_cut_1_r_0X2 = p_0_cut_1.parse("0X2");
		
		assertThat(p_0_1_r_0X2.isSuccessful(), is(false));
		assertThat(p_0_1_r_0X2.canBacktrack(), is(true));
		assertThat(p_0_1_r_0X2.end().index(), is(1));
		
		assertThat(p_0_cut_1_r_0X2.isSuccessful(), is(false));
		assertThat(p_0_cut_1_r_0X2.canBacktrack(), is(false));
		assertThat(p_0_cut_1_r_0X2.end().index(), is(1));
	}

}
