package org.unclazz.parsec.sample;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.unclazz.parsec.sample.json.Json;

public class JsonParserTest {
	@Test
	public void testParseString() {
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
	
	@Test
	public void testParseInputStreamCharset() {
		try (final InputStream validJson = jsonStream("sample0_valid.json")) {
			final Json fromFile = Json.parse(validJson, Charset.forName("utf-8"));
			assertThat(fromFile.objectPropery("foo").numberValue(), is(123.0));
			assertThat(fromFile.objectPropery("baz").objectPropery("baz/foo").stringValue(), is("123"));
		} catch (final IOException e) {
			fail(e.getMessage());
		}
		try (final InputStream invalidJson = jsonStream("sample1_invalid.json")) {
			Json.parse(invalidJson, Charset.forName("utf-8"));
			fail("invalid json.");
		} catch (RuntimeException e) {
			// OK
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}
	
	private InputStream jsonStream(String filename) {
		return getClass().getResourceAsStream(filename);
	}
}
