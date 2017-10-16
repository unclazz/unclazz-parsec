package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class SkipToParserTest {

	@Test
	public void test() {
		final Parser skipToA = Parsers.skipTo(Parsers.exact('a'));
		
		final Result result0 = skipToA.parse("0123456789abcdef");
		assertTrue(result0.isSuccessful());
		assertThat(result0.start().index(), is(10));
		assertThat(result0.end().index(), is(11));
		
		final Result result1 = skipToA.parse("0123456789ABCDEF");
		assertFalse(result1.isSuccessful());
		assertThat(result1.end().index(), is(16));
		
		class CommentParser extends Parser {
			@Override
			protected ResultCore doParse(Context ctx) throws IOException {
				return keyword("/*").then(skipTo(keyword("*/"))).parse(ctx);
			}
		}
		
		final Parser comment = new CommentParser();
		assertTrue(comment.parse("/* comment text */ not comment text").isSuccessful());
		assertTrue(comment.parse("/* comment text /* */ not comment text").isSuccessful());
		assertTrue(comment.parse("/* comment text */ */ not comment text").isSuccessful());
		assertFalse(comment.parse("/* comment text ").isSuccessful());
	}

}
