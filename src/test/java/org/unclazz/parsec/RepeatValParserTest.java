package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class RepeatValParserTest {
	private String list2String(List<String> list) {
		return list.stream().reduce((a, b) -> a + b).orElse("");
	}
	@Test
	public void testParseTextReader_rep() throws IOException {
		// Arrange
		final RepeatValParser<String> p_a_rep = Parsers.exact('a').val().rep();
		final RepeatValParser<String> p_ab_rep = Parsers.charIn("ab").val().rep();
		final RepeatValParser<String> p_b_rep = Parsers.exact('b').val().rep();
		
		// Act
		final ValResult<List<String>> r_a_rep = p_a_rep.parse("abbccc");
		final ValResult<List<String>> r_ab_rep = p_ab_rep.parse("abbccc");
		final ValResult<List<String>> r_b_rep = p_b_rep.parse("abbccc");
		
		// Assert
		assertThat(r_a_rep.isSuccessful(), is(true));
		assertThat(list2String(r_a_rep.value()), is("a"));
		assertThat(r_a_rep.end().index(), is(1));
		
		assertThat(r_ab_rep.isSuccessful(), is(true));
		assertThat(list2String(r_ab_rep.value()), is("abb"));
		assertThat(r_ab_rep.end().index(), is(3));
		
		assertThat(r_b_rep.isSuccessful(), is(true));
		assertThat(list2String(r_b_rep.value()), is(""));
		assertThat(r_b_rep.end().index(), is(0));
	}

	@Test
	public void testParseTextReader_repParser() throws IOException {
		// Arrange
		final RepeatValParser<String> p_a_rep = Parsers.exact('a').val().rep(Parsers.exact(','));
		final RepeatValParser<String> p_ab_rep = Parsers.charIn("ab").val().rep(Parsers.exact(','));
		final RepeatValParser<String> p_b_rep = Parsers.exact('b').val().rep(Parsers.exact(','));
		
		// Act
		final ValResult<List<String>> r_a_rep = p_a_rep.parse("a,bbccc");
		final ValResult<List<String>> r_ab_rep = p_ab_rep.parse("a,b,b,ccc");
		final ValResult<List<String>> r_b_rep = p_b_rep.parse("a,b,b,ccc");
		
		// Assert
		assertThat(r_a_rep.isSuccessful(), is(true));
		assertThat(list2String(r_a_rep.value()), is("a"));
		assertThat(r_a_rep.end().index(), is(1));
		
		assertThat(r_ab_rep.isSuccessful(), is(true));
		assertThat(list2String(r_ab_rep.value()), is("abb"));
		assertThat(r_ab_rep.end().index(), is(5));
		
		assertThat(r_b_rep.isSuccessful(), is(true));
		assertThat(r_b_rep.end().index(), is(0));
	}

	@Test
	public void testParseTextReader_repInt() throws IOException {
		// Arrange
		final RepeatValParser<String> p_a_rep3 = Parsers.exact('a').val().rep(3);
		
		// Act
		final ValResult<List<String>> r_a_rep3_vs_abbccc = p_a_rep3.parse("abbccc");
		final ValResult<List<String>> r_a_rep3_vs_aabccc = p_a_rep3.parse("aabccc");
		final ValResult<List<String>> r_a_rep3_vs_aaaccc = p_a_rep3.parse("aaaccc");
		final ValResult<List<String>> r_a_rep3_vs_aaaacc = p_a_rep3.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep3_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep3_vs_abbccc.end().index(), is(1));
		
		assertThat(r_a_rep3_vs_aabccc.isSuccessful(), is(false));
		assertThat(r_a_rep3_vs_aabccc.end().index(), is(2));
		
		assertThat(r_a_rep3_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep3_vs_aaaccc.end().index(), is(3));
		
		assertThat(r_a_rep3_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep3_vs_aaaacc.end().index(), is(3));
	}

	@Test
	public void testParseTextReader_repIntInt() throws IOException {
		// Arrange
		final RepeatValParser<String> p_a_rep23 = Parsers.exact('a').val().rep(2, 3);
		
		// Act
		final ValResult<List<String>> r_a_rep23_vs_abbccc = p_a_rep23.parse("abbccc");
		final ValResult<List<String>> r_a_rep23_vs_aabccc = p_a_rep23.parse("aabccc");
		final ValResult<List<String>> r_a_rep23_vs_aaaccc = p_a_rep23.parse("aaaccc");
		final ValResult<List<String>> r_a_rep23_vs_aaaacc = p_a_rep23.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep23_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep23_vs_abbccc.end().index(), is(1));
		
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
		final RepeatValParser<String> p_a_rep23 = Parsers.exact('a').val().rep(2, 3, Parsers.exact(','));
		
		// Act
		final ValResult<List<String>> r_a_rep23_vs_abbccc = p_a_rep23.parse("a,b,b,ccc");
		final ValResult<List<String>> r_a_rep23_vs_aabccc = p_a_rep23.parse("a,a,b,ccc");
		final ValResult<List<String>> r_a_rep23_vs_aaaccc = p_a_rep23.parse("a,a,a,ccc");
		final ValResult<List<String>> r_a_rep23_vs_aaaacc = p_a_rep23.parse("a,a,a,acc");
		
		// Assert
		assertThat(r_a_rep23_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep23_vs_abbccc.end().index(), is(2));
		
		assertThat(r_a_rep23_vs_aabccc.isSuccessful(), is(true));
		assertThat(list2String(r_a_rep23_vs_aabccc.value()), is("aa"));
		assertThat(r_a_rep23_vs_aabccc.end().index(), is(3));
		
		assertThat(r_a_rep23_vs_aaaccc.isSuccessful(), is(true));
		assertThat(list2String(r_a_rep23_vs_aaaccc.value()), is("aaa"));
		assertThat(r_a_rep23_vs_aaaccc.end().index(), is(5));
		
		assertThat(r_a_rep23_vs_aaaacc.isSuccessful(), is(true));
		assertThat(list2String(r_a_rep23_vs_aaaacc.value()), is("aaa"));
		assertThat(r_a_rep23_vs_aaaacc.end().index(), is(5));
	}

	@Test
	public void testParseTextReader_repMinInt() throws IOException {
		// Arrange
		final RepeatValParser<String> p_a_rep2 = Parsers.exact('a').val().repMin(2);
		
		// Act
		final ValResult<List<String>> r_a_rep2_vs_abbccc = p_a_rep2.parse("abbccc");
		final ValResult<List<String>> r_a_rep2_vs_aabccc = p_a_rep2.parse("aabccc");
		final ValResult<List<String>> r_a_rep2_vs_aaaccc = p_a_rep2.parse("aaaccc");
		final ValResult<List<String>> r_a_rep2_vs_aaaacc = p_a_rep2.parse("aaaacc");
		
		// Assert
		assertThat(r_a_rep2_vs_abbccc.isSuccessful(), is(false));
		assertThat(r_a_rep2_vs_abbccc.end().index(), is(1));
		
		assertThat(r_a_rep2_vs_aabccc.isSuccessful(), is(true));
		assertThat(r_a_rep2_vs_aabccc.end().index(), is(2));
		
		assertThat(r_a_rep2_vs_aaaccc.isSuccessful(), is(true));
		assertThat(r_a_rep2_vs_aaaccc.end().index(), is(3));
		
		assertThat(r_a_rep2_vs_aaaacc.isSuccessful(), is(true));
		assertThat(r_a_rep2_vs_aaaacc.end().index(), is(4));
	}
	
	@Test
	public void testReduce1() throws IOException {
		// Arrange
		final ValParser<Optional<Integer>> p = Parsers.charIn("0123")
				.map(Integer::parseInt).rep().reduce((a, b) -> a + b);
		
		// Act
		final ValResult<Optional<Integer>> r_012345 = p.parse("012345");
		final ValResult<Optional<Integer>> r_333450 = p.parse("333450");
		final ValResult<Optional<Integer>> r_450123 = p.parse("450123");
		
		// Assert
		assertThat(r_012345.isSuccessful(), is(true));
		assertThat(r_012345.value().get(), is(6));
		
		assertThat(r_333450.isSuccessful(), is(true));
		assertThat(r_333450.value().get(), is(9));

		assertThat(r_450123.isSuccessful(), is(true));
		assertThat(r_450123.value().isPresent(), is(false));
	}
	
	@Test
	public void testReduce2() throws IOException {
		// Arrange
		final ValParser<Integer> p = Parsers.charIn("0123")
				.map(Integer::parseInt).rep().reduce(() -> 2, (a, b) -> a + b);
		
		// Act
		final ValResult<Integer> r_012345 = p.parse("012345");
		final ValResult<Integer> r_333450 = p.parse("333450");
		final ValResult<Integer> r_450123 = p.parse("450123");
		
		// Assert
		assertThat(r_012345.isSuccessful(), is(true));
		assertThat(r_012345.value(), is(8));
		
		assertThat(r_333450.isSuccessful(), is(true));
		assertThat(r_333450.value(), is(11));

		assertThat(r_450123.isSuccessful(), is(true));
		assertThat(r_450123.value(), is(2));
	}
	
	@Test
	public void testReduce3() throws IOException {
		// Arrange
		final ValParser<String> p = Parsers.charIn("0123")
				.map(Integer::parseInt).rep().reduce(() -> 2, (a, b) -> a + b, c -> c.toString());
		
		// Act
		final ValResult<String> r_012345 = p.parse("012345");
		final ValResult<String> r_333450 = p.parse("333450");
		final ValResult<String> r_450123 = p.parse("450123");
		
		// Assert
		assertThat(r_012345.isSuccessful(), is(true));
		assertThat(r_012345.value(), is("8"));
		
		assertThat(r_333450.isSuccessful(), is(true));
		assertThat(r_333450.value(), is("11"));

		assertThat(r_450123.isSuccessful(), is(true));
		assertThat(r_450123.value(), is("2"));
	}

}
