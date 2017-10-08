package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class OrParserTest {

	@Test
	public void testParseString() {
		final Parser p_01 = Parsers.exact('0').then(Parsers.exact('1'));
		final Parser p_0cut1 = Parsers.exact('0').cut().then(Parsers.exact('1'));
		final Parser p_02 = Parsers.exact('0').then(Parsers.exact('2'));
		
		final Parser p_01_or_02 = p_01.or(p_02);
		final Parser p_0cut1_or_02 = p_0cut1.or(p_02);
		
		final Result p_01_or_02_r_01 = p_01_or_02.parse("01_");
		final Result p_0cut1_or_02_r01 = p_0cut1_or_02.parse("01_");
		
		assertThat(p_01_or_02_r_01.isSuccessful(), is(true));
		assertThat(p_01_or_02_r_01.canBacktrack(), is(true));
		assertThat(p_01_or_02_r_01.end().index(), is(2));
		
		assertThat(p_0cut1_or_02_r01.isSuccessful(), is(true));
		assertThat(p_0cut1_or_02_r01.canBacktrack(), is(true));
		assertThat(p_0cut1_or_02_r01.end().index(), is(2));
		
		final Result p_01_or_02_r_02 = p_01_or_02.parse("02_");
		final Result p_0cut1_or_02_r02 = p_0cut1_or_02.parse("02_");
		
		assertThat(p_01_or_02_r_02.isSuccessful(), is(true));
		assertThat(p_01_or_02_r_02.canBacktrack(), is(true));
		assertThat(p_01_or_02_r_02.end().index(), is(2));
		assertThat(p_01_or_02_r_02.message(), is(nullValue()));
		
		assertThat(p_0cut1_or_02_r02.isSuccessful(), is(false));
		assertThat(p_0cut1_or_02_r02.canBacktrack(), is(true));
		assertThat(p_0cut1_or_02_r02.end().index(), is(1));
		assertThat(p_0cut1_or_02_r02.message(), is("'1'(49) expected but '2'(50) found."));
		
		final Parser p_bool = Parsers.keyword("true", 1).or(Parsers.keyword("false"));
		assertThat(p_bool.parse("true").message(), is(nullValue()));
		assertThat(p_bool.parse("tlue").message(), is("'r'(114) expected but 'l'(108) found."));
		assertThat(p_bool.parse("trve").message(), is("'u'(117) expected but 'v'(118) found."));
		assertThat(p_bool.parse("false").message(), is(nullValue()));
		assertThat(p_bool.parse("halse").message(), is("'f'(102) expected but 'h'(104) found."));
	}

}
