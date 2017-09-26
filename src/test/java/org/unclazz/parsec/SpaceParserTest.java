package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class SpaceParserTest {

	@Test
	public void testParseString() {
		final Parser p_sp_0 = Parsers.space(0);
		final Parser p_sp_1 = Parsers.space(1);
		
		final Result p_sp_0_r_0spaces = p_sp_0.parse("___");
		final Result p_sp_0_r_1spaces = p_sp_0.parse(" ___");
		final Result p_sp_0_r_2spaces = p_sp_0.parse("  ___");
		final Result p_sp_1_r_0spaces = p_sp_1.parse("___");
		final Result p_sp_1_r_1spaces = p_sp_1.parse(" ___");
		final Result p_sp_1_r_2spaces = p_sp_1.parse("  ___");
		
		assertThat(p_sp_0_r_0spaces.isSuccessful(), is(true));
		assertThat(p_sp_0_r_0spaces.end().index(), is(0));
		assertThat(p_sp_0_r_1spaces.isSuccessful(), is(true));
		assertThat(p_sp_0_r_1spaces.end().index(), is(1));
		assertThat(p_sp_0_r_2spaces.isSuccessful(), is(true));
		assertThat(p_sp_0_r_2spaces.end().index(), is(2));
		
		assertThat(p_sp_1_r_0spaces.isSuccessful(), is(false));
		assertThat(p_sp_1_r_0spaces.end().index(), is(0));
		assertThat(p_sp_1_r_1spaces.isSuccessful(), is(true));
		assertThat(p_sp_1_r_1spaces.end().index(), is(1));
		assertThat(p_sp_1_r_2spaces.isSuccessful(), is(true));
		assertThat(p_sp_1_r_2spaces.end().index(), is(2));
	}

}
