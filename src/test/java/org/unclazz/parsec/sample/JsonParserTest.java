package org.unclazz.parsec.sample;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.unclazz.parsec.sample.json.Json;

public class JsonParserTest {
	@Test
	public void testParse() {
		final Json falseValue = Json.parse("false");
		assertThat(falseValue.isBoolean(), is(true));
		assertThat(falseValue.booleanValue(), is(false));

		final Json number123 = Json.parse(" 123 ");
		assertThat(number123.isNumber(), is(true));
		assertThat(number123.numberValue(), is(123.0));
		
		final Json emptyArray = Json.parse("[]");
		assertThat(emptyArray.isArray(), is(true));
		assertThat(emptyArray.arrayElements().isEmpty(), is(true));

		final Json array123 = Json.parse("[1,2,3]");
		assertThat(array123.isArray(), is(true));
		assertThat(array123.arrayElements().stream().map(a -> a.numberValue()).toArray(),
				is(new double[] {1.0,2.0,3.0}));
	}
}
