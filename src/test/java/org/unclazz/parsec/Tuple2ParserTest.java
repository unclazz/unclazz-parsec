package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.parsec.data.Tuple2;

public class Tuple2ParserTest {

	@Test
	public void testParseString() {
		final ValParser<Tuple2<String, String>> p_0_1 = Parsers.exact('0').val().then(Parsers.exact('1').val());
		final ValParser<Tuple2<String, String>> p_0_cut_1 = Parsers.exact('0').val().cut().then(Parsers.exact('1').val());
		
		final ValResult<Tuple2<String, String>> p_0_1_r_012 = p_0_1.parse("012");
		final ValResult<Tuple2<String, String>> p_0_cut_1_r_012 = p_0_cut_1.parse("012");
		
		assertThat(p_0_1_r_012.isSuccessful(), is(true));
		assertThat(p_0_1_r_012.canBacktrack(), is(true));
		assertThat(p_0_1_r_012.end().index(), is(2));
		assertThat(p_0_1_r_012.value().item1(), is("0"));
		assertThat(p_0_1_r_012.value().item2(), is("1"));
		
		assertThat(p_0_cut_1_r_012.isSuccessful(), is(true));
		assertThat(p_0_cut_1_r_012.canBacktrack(), is(false));
		assertThat(p_0_cut_1_r_012.end().index(), is(2));
		assertThat(p_0_cut_1_r_012.value().item1(), is("0"));
		assertThat(p_0_cut_1_r_012.value().item2(), is("1"));
		
		final ValResult<Tuple2<String, String>> p_0_1_r_X12 = p_0_1.parse("X12");
		final ValResult<Tuple2<String, String>> p_0_cut_1_r_X12 = p_0_cut_1.parse("X12");
		
		assertThat(p_0_1_r_X12.isSuccessful(), is(false));
		assertThat(p_0_1_r_X12.canBacktrack(), is(true));
		assertThat(p_0_1_r_X12.end().index(), is(1));
		assertThat(p_0_1_r_X12.value(), is(nullValue()));
		
		assertThat(p_0_cut_1_r_X12.isSuccessful(), is(false));
		assertThat(p_0_cut_1_r_X12.canBacktrack(), is(true));
		assertThat(p_0_cut_1_r_X12.end().index(), is(1));
		assertThat(p_0_cut_1_r_X12.value(), is(nullValue()));
		
		final ValResult<Tuple2<String, String>> p_0_1_r_0X2 = p_0_1.parse("0X2");
		final ValResult<Tuple2<String, String>> p_0_cut_1_r_0X2 = p_0_cut_1.parse("0X2");
		
		assertThat(p_0_1_r_0X2.isSuccessful(), is(false));
		assertThat(p_0_1_r_0X2.canBacktrack(), is(true));
		assertThat(p_0_1_r_0X2.end().index(), is(2));
		assertThat(p_0_1_r_0X2.value(), is(nullValue()));

		assertThat(p_0_cut_1_r_0X2.isSuccessful(), is(false));
		assertThat(p_0_cut_1_r_0X2.canBacktrack(), is(false));
		assertThat(p_0_cut_1_r_0X2.end().index(), is(2));
		assertThat(p_0_cut_1_r_0X2.value(), is(nullValue()));
	}

}
