package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class KeywordInParserTest {

	@Test
	public void testParseTextReader() {
		final Parser p = Parsers.keywordIn("aaab", "aabb", "abbb", "aacc", "ccbb");
		
		final Result r_aaaa = p.parse("aaaa");
		final Result r_aaab = p.parse("aaab");
		final Result r_aabb = p.parse("aabb");
		final Result r_aacc = p.parse("aacc");
		final Result r_abbb = p.parse("abbb");
		final Result r_bbbb = p.parse("bbbb");
		
		assertThat(r_aaaa.isSuccessful(), is(false));
		assertThat(r_aaab.isSuccessful(), is(true));
		assertThat(r_aabb.isSuccessful(), is(true));
		assertThat(r_aacc.isSuccessful(), is(true));
		assertThat(r_abbb.isSuccessful(), is(true));
		assertThat(r_bbbb.isSuccessful(), is(false));
		
		final Result r_aaax = p.parse("aaax");
		
		assertThat(r_aaax.isSuccessful(), is(false));
		assertThat(r_aaax.message(), is("'b'(98) expected but 'x'(120) found."));
		assertThat(r_aaax.end().index(), is(3));

		final Result r_aabx = p.parse("aaby");
		assertThat(r_aabx.isSuccessful(), is(false));
		assertThat(r_aabx.message(), is("'b'(98) expected but 'y'(121) found."));
		
		final Result r_abbx = p.parse("abbz");
		assertThat(r_abbx.isSuccessful(), is(false));
		assertThat(r_abbx.message(), is("'b'(98) expected but 'z'(122) found."));
	}
	@Test
	public void testCostructor_abend() {
		try {
			final String[] a = null;
			Parsers.keywordIn(a);
		} catch (NullPointerException e) {
			// OK
		}
		try {
			Parsers.keywordIn("aaab", "aabb", "abbb", "aacc", "");
		} catch (IllegalArgumentException e) {
			// OK
		}
		try {
			Parsers.keywordIn("aaab", "aabb", "abbb", "aacc", "aabb");
		} catch (IllegalArgumentException e) {
			// OK
		}
		try {
			Parsers.keywordIn("aaab", null, "abbb", "aacc", "");
		} catch (IllegalArgumentException e) {
			// OK
		}
		try {
			Parsers.keywordIn();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}
}
