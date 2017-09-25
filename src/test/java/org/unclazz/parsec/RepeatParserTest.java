package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class RepeatParserTest {

	@Test
	public void testParseTextReader_rep() throws IOException {
		// Arrange
		final Parser p_a_rep = Parsers.exact('a').rep();
		final Parser p_ab_rep = Parsers.charIn("ab").rep();
		final Parser p_b_rep = Parsers.exact('b').rep();
		
		// Act
		final Result r_a_rep = p_a_rep.parse("abbccc");
		final Result r_ab_rep = p_ab_rep.parse("abbccc");
		final Result r_b_rep = p_b_rep.parse("abbccc");
		
		// Assert
		assertThat(r_a_rep.isSuccessful(), is(true));
		assertThat(r_a_rep.end().index(), is(1));
		
		assertThat(r_ab_rep.isSuccessful(), is(true));
		assertThat(r_ab_rep.end().index(), is(3));
		
		assertThat(r_b_rep.isSuccessful(), is(true));
		assertThat(r_b_rep.end().index(), is(0));
	}

	@Test
	public void testParseTextReader_repParser() throws IOException {
		// Arrange
		final Parser p_a_rep = Parsers.exact('a').rep(Parsers.exact(','));
		final Parser p_ab_rep = Parsers.charIn("ab").rep(Parsers.exact(','));
		final Parser p_b_rep = Parsers.exact('b').rep(Parsers.exact(','));
		
		// Act
		final Result r_a_rep = p_a_rep.parse("a,bbccc");
		final Result r_ab_rep = p_ab_rep.parse("a,b,b,ccc");
		final Result r_b_rep = p_b_rep.parse("a,b,b,ccc");
		
		// Assert
		assertThat(r_a_rep.isSuccessful(), is(true));
		assertThat(r_a_rep.end().index(), is(1));
		
		assertThat(r_ab_rep.isSuccessful(), is(true));
		assertThat(r_ab_rep.end().index(), is(5));
		
		assertThat(r_b_rep.isSuccessful(), is(true));
		assertThat(r_b_rep.end().index(), is(0));
	}

	@Test
	public void testParseTextReader_repInt() throws IOException {
		// Arrange
		final Parser p_a_rep2 = Parsers.exact('a').rep(3);
		
		// Act
		final Result r_a_rep2_vs_abbccc = p_a_rep2.parse("abbccc");
		final Result r_a_rep2_vs_aabccc = p_a_rep2.parse("aabccc");
		final Result r_a_rep2_vs_aaaccc = p_a_rep2.parse("aaaccc");
		final Result r_a_rep2_vs_aaaacc = p_a_rep2.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep2_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep2_vs_abbccc.end().index(), is(2));
		
		assertThat(r_a_rep2_vs_aabccc.isSuccessful(), is(false));
		assertThat(r_a_rep2_vs_aabccc.end().index(), is(3));
		
		assertThat(r_a_rep2_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep2_vs_aaaccc.end().index(), is(3));
		
		assertThat(r_a_rep2_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep2_vs_aaaacc.end().index(), is(3));
	}

	@Test
	public void testParseTextReader_repIntInt() throws IOException {
		// Arrange
		final Parser p_a_rep2 = Parsers.exact('a').rep(2, 3);
		
		// Act
		final Result r_a_rep23_vs_abbccc = p_a_rep2.parse("abbccc");
		final Result r_a_rep23_vs_aabccc = p_a_rep2.parse("aabccc");
		final Result r_a_rep23_vs_aaaccc = p_a_rep2.parse("aaaccc");
		final Result r_a_rep23_vs_aaaacc = p_a_rep2.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep23_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep23_vs_abbccc.end().index(), is(2));
		
		assertThat(r_a_rep23_vs_aabccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aabccc.end().index(), is(2));
		
		assertThat(r_a_rep23_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaccc.end().index(), is(3));
		
		assertThat(r_a_rep23_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaacc.end().index(), is(3));
	}

	@Test
	public void testParseTextReader_repIntIntParser() throws IOException {
		// Arrange
		final Parser p_a_rep2 = Parsers.exact('a').rep(2, 3, Parsers.exact(','));
		
		// Act
		final Result r_a_rep23_vs_abbccc = p_a_rep2.parse("a,b,b,ccc");
		final Result r_a_rep23_vs_aabccc = p_a_rep2.parse("a,a,b,ccc");
		final Result r_a_rep23_vs_aaaccc = p_a_rep2.parse("a,a,a,ccc");
		final Result r_a_rep23_vs_aaaacc = p_a_rep2.parse("a,a,a,acc");
		
		// Assert
		assertThat(r_a_rep23_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep23_vs_abbccc.end().index(), is(3));
		
		assertThat(r_a_rep23_vs_aabccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aabccc.end().index(), is(3));
		
		assertThat(r_a_rep23_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaccc.end().index(), is(5));
		
		assertThat(r_a_rep23_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaacc.end().index(), is(5));
	}

	@Test
	public void testParseTextReader_repMinInt() throws IOException {
		// Arrange
		final Parser p_a_rep2 = Parsers.exact('a').repMin(2);
		
		// Act
		final Result r_a_rep23_vs_abbccc = p_a_rep2.parse("abbccc");
		final Result r_a_rep23_vs_aabccc = p_a_rep2.parse("aabccc");
		final Result r_a_rep23_vs_aaaccc = p_a_rep2.parse("aaaccc");
		final Result r_a_rep23_vs_aaaacc = p_a_rep2.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep23_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep23_vs_abbccc.end().index(), is(2));
		
		assertThat(r_a_rep23_vs_aabccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aabccc.end().index(), is(2));
		
		assertThat(r_a_rep23_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaccc.end().index(), is(3));
		
		assertThat(r_a_rep23_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep23_vs_aaaacc.end().index(), is(4));
	}

}
