package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Before;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class MapValParserTest {

	private int checkNum = 0;
	
	@Before
	public void setUp() {
		checkNum = 0;
	}
	
	@Test
	public void testParseString_cannotThrow() {
		final ValParser<Integer> p_123_map = Parsers.keyword("123").map(a -> {
			checkNum = 1;
			return Integer.parseInt(a);
		});
		final ValParser<Integer> p_hello_map = Parsers.keyword("hello").map(a -> {
			checkNum = 2;
			return Integer.parseInt(a);
		});
		
		final ValResult<Integer> p_123_map_r_hello = p_123_map.parse("hello");
		assertThat(checkNum, is(0));
		assertThat(p_123_map_r_hello.isSuccessful(), is(false));
		
		final ValResult<Integer> p_123_map_r_123 = p_123_map.parse("123");
		assertThat(checkNum, is(1));
		assertThat(p_123_map_r_123.isSuccessful(), is(true));
		assertThat(p_123_map_r_123.value(), is(123));
		
		final ValResult<Integer> p_hello_map_r_hello = p_hello_map.parse("hello");
		assertThat(checkNum, is(2));
		assertThat(p_hello_map_r_hello.value(), is(nullValue()));
		assertThat(p_hello_map_r_hello.isSuccessful(), is(false));
	}
	
	@Test
	public void testParseString_canThrow() {
		final ValParser<Integer> p_123_map = Parsers.keyword("123").map(a -> {
			checkNum = 1;
			return Integer.parseInt(a);
		}, true);
		final ValParser<Integer> p_hello_map = Parsers.keyword("hello").map(a -> {
			checkNum = 2;
			return Integer.parseInt(a);
		}, true);
		
		final ValResult<Integer> p_123_map_r_hello = p_123_map.parse("hello");
		assertThat(checkNum, is(0));
		assertThat(p_123_map_r_hello.isSuccessful(), is(false));
		
		final ValResult<Integer> p_123_map_r_123 = p_123_map.parse("123");
		assertThat(checkNum, is(1));
		assertThat(p_123_map_r_123.isSuccessful(), is(true));
		assertThat(p_123_map_r_123.value(), is(123));
		
		try {
			p_hello_map.parse("hello");
			fail();
		} catch (final NumberFormatException e) {
			// OK.
		}
	}

}
