package org.unclazz.parsec.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.unclazz.parsec.Context;
import org.unclazz.parsec.ValParser;
import org.unclazz.parsec.ValResult;
import org.unclazz.parsec.ValResultCore;

public final class HelloParserTest {

	@Test
	public void testParse() {
		final ValResult<String> r = new HelloParser().parse("hello_");
		assertThat(r.isSuccessful(), is(true));
		assertThat(r.value(), is("hello"));
		assertThat(r.message(), is(nullValue()));
	}

}

final class HelloParser extends ValParser<String>{
	@Override
	protected ValResultCore<String> doParse(Context ctx) throws IOException {
		ctx = ctx.configure(a -> a.setLogAppender(System.out::println));
		final ValResult<String> r = keyword("hello").val().parse(ctx);
		return r;
	}
}