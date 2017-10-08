package org.unclazz.parsec.sample;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

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
	
	@Test
	public void testOfNull() {
		final Json j0 = Json.ofNull();
		
		assertThat(j0.isArray(), is(false));
		assertThat(j0.isBoolean(), is(false));
		assertThat(j0.isNull(), is(true));
		assertThat(j0.isNumber(), is(false));
		assertThat(j0.isObject(), is(false));
		assertThat(j0.isString(), is(false));
		
		mustThrow(() -> j0.arrayElement(0), IllegalStateException.class);
		mustNotThrow(() -> j0.arrayElement(0, Json.of(1)), IllegalStateException.class);
		mustThrow(() -> j0.arrayElements(), IllegalStateException.class);
		mustThrow(() -> j0.booleanValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.booleanValue(false), IllegalStateException.class);
		mustThrow(() -> j0.numberValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.numberValue(2), IllegalStateException.class);
		mustThrow(() -> j0.objectProperties(), IllegalStateException.class);
		mustNotThrow(() -> j0.objectProperties(Collections.emptyMap()), IllegalStateException.class);
		mustThrow(() -> j0.objectPropery("foo"), IllegalStateException.class);
		mustNotThrow(() -> j0.objectPropery("foo", Json.of(3)), IllegalStateException.class);
		mustThrow(() -> j0.stringValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.stringValue("bar"), IllegalStateException.class);
	}
	
	@Test
	public void testOfBoolean() {
		final Json j0 = Json.of(true);
		assertThat(j0.booleanValue(), is(true));
		
		assertThat(j0.isArray(), is(false));
		assertThat(j0.isBoolean(), is(true));
		assertThat(j0.isNull(), is(false));
		assertThat(j0.isNumber(), is(false));
		assertThat(j0.isObject(), is(false));
		assertThat(j0.isString(), is(false));
		
		mustThrow(() -> j0.arrayElement(0), IllegalStateException.class);
		mustNotThrow(() -> j0.arrayElement(0, Json.of(1)), IllegalStateException.class);
		mustThrow(() -> j0.arrayElements(), IllegalStateException.class);
		mustNotThrow(() -> j0.booleanValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.booleanValue(false), IllegalStateException.class);
		mustThrow(() -> j0.numberValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.numberValue(2), IllegalStateException.class);
		mustThrow(() -> j0.objectProperties(), IllegalStateException.class);
		mustNotThrow(() -> j0.objectProperties(Collections.emptyMap()), IllegalStateException.class);
		mustThrow(() -> j0.objectPropery("foo"), IllegalStateException.class);
		mustNotThrow(() -> j0.objectPropery("foo", Json.of(3)), IllegalStateException.class);
		mustThrow(() -> j0.stringValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.stringValue("bar"), IllegalStateException.class);
	}
	
	@Test
	public void testOfDouble() {
		final Json j0 = Json.of(123);
		assertThat(j0.numberValue(), is(123.0));
		assertThat(j0.numberValue(456.0), is(123.0));
		
		assertThat(j0.isArray(), is(false));
		assertThat(j0.isBoolean(), is(false));
		assertThat(j0.isNull(), is(false));
		assertThat(j0.isNumber(), is(true));
		assertThat(j0.isObject(), is(false));
		assertThat(j0.isString(), is(false));
		
		mustThrow(() -> j0.arrayElement(0), IllegalStateException.class);
		mustNotThrow(() -> j0.arrayElement(0, Json.of(1)), IllegalStateException.class);
		mustThrow(() -> j0.arrayElements(), IllegalStateException.class);
		mustThrow(() -> j0.booleanValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.booleanValue(false), IllegalStateException.class);
		mustNotThrow(() -> j0.numberValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.numberValue(2), IllegalStateException.class);
		mustThrow(() -> j0.objectProperties(), IllegalStateException.class);
		mustNotThrow(() -> j0.objectProperties(Collections.emptyMap()), IllegalStateException.class);
		mustThrow(() -> j0.objectPropery("foo"), IllegalStateException.class);
		mustNotThrow(() -> j0.objectPropery("foo", Json.of(3)), IllegalStateException.class);
		mustThrow(() -> j0.stringValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.stringValue("bar"), IllegalStateException.class);
	}
	
	@Test
	public void testOfString() {
		final Json j0 = Json.of("baz");
		assertThat(j0.stringValue(), is("baz"));
		assertThat(j0.stringValue("foo"), is("baz"));
		
		assertThat(j0.isArray(), is(false));
		assertThat(j0.isBoolean(), is(false));
		assertThat(j0.isNull(), is(false));
		assertThat(j0.isNumber(), is(false));
		assertThat(j0.isObject(), is(false));
		assertThat(j0.isString(), is(true));
		
		mustThrow(() -> j0.arrayElement(0), IllegalStateException.class);
		mustNotThrow(() -> j0.arrayElement(0, Json.of(1)), IllegalStateException.class);
		mustThrow(() -> j0.arrayElements(), IllegalStateException.class);
		mustThrow(() -> j0.booleanValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.booleanValue(false), IllegalStateException.class);
		mustThrow(() -> j0.numberValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.numberValue(2), IllegalStateException.class);
		mustThrow(() -> j0.objectProperties(), IllegalStateException.class);
		mustNotThrow(() -> j0.objectProperties(Collections.emptyMap()), IllegalStateException.class);
		mustThrow(() -> j0.objectPropery("foo"), IllegalStateException.class);
		mustNotThrow(() -> j0.objectPropery("foo", Json.of(3)), IllegalStateException.class);
		mustNotThrow(() -> j0.stringValue(), IllegalStateException.class);
		mustNotThrow(() -> j0.stringValue("bar"), IllegalStateException.class);
	}
	
	@Test
	public void testParseString_forNull() {
		final Json j0 = Json.parse("  null   ");
		assertThat(j0.isNull(), is(true));
	}
	
	@Test
	public void testParseString_forBoolean() {
		final Json j0 = Json.parse("     true ");
		assertThat(j0.isBoolean(), is(true));
		assertThat(j0.booleanValue(), is(true));
	}
	
	@Test
	public void testParseString_forNumber() {
		final Json j0 = Json.parse("   -123.456 ");
		final Json j1 = Json.parse("1.23e-4 ");
		assertThat(j0.isNumber(), is(true));
		assertThat(j0.numberValue(), is(-123.456));
		assertThat(j1.isNumber(), is(true));
		assertThat(j1.numberValue(), is(1.23e-4));
	}
	
	@Test
	public void testParseString_forString() {
		final Json j0 = Json.parse("  \"foo \\\"bar\\\" baz\" ");
		assertThat(j0.isString(), is(true));
		assertThat(j0.stringValue(), is("foo \"bar\" baz"));

		final Json j1 = Json.parse("  \"\" ");
		assertThat(j1.isString(), is(true));
		assertThat(j1.stringValue(), is(""));
		
		final Json j2 = Json.parse("  \"\\r\\n \uD867\uDE3D\" ");
		assertThat(j2.isString(), is(true));
		assertThat(j2.stringValue(), is("\r\n 𩸽"));
	}
	
	@Test
	public void testParseString_forArray() {
		final Json j0 = Json.parse("   [null,false,0.0,\"\",[],{}] ");
		assertThat(j0.isArray(), is(true));
		assertThat(j0.arrayElement(0).isNull(), is(true));
		assertThat(j0.arrayElement(1).booleanValue(), is(false));
		assertThat(j0.arrayElement(2).numberValue(), is(0.0));
		assertThat(j0.arrayElement(3).stringValue(), is(""));
		assertThat(j0.arrayElement(4).arrayElements().isEmpty(), is(true));
		assertThat(j0.arrayElement(5).objectProperties().isEmpty(), is(true));

		final Json j1 = Json.parse("   [null,true,1.0,\"a\",[[]],{\"a\": {}}] ");
		assertThat(j1.isArray(), is(true));
		assertThat(j1.arrayElement(0).isNull(), is(true));
		assertThat(j1.arrayElement(1).booleanValue(), is(true));
		assertThat(j1.arrayElement(2).numberValue(), is(1.0));
		assertThat(j1.arrayElement(3).stringValue(), is("a"));
		assertThat(j1.arrayElement(4).arrayElement(0).arrayElements().isEmpty(), is(true));
		assertThat(j1.arrayElement(5).objectPropery("a").objectProperties().isEmpty(), is(true));
	}
	
	private void mustThrow(Runnable action, Class<? extends Exception> exClazz) {
		try {
			action.run();
		} catch (Exception ex) {
			assertThat(ex, instanceOf(exClazz));
		}
	}
	
	private void mustNotThrow(Runnable action, Class<? extends Exception> exClazz) {
		try {
			action.run();
		} catch (Exception ex) {
			assertThat(ex, not(instanceOf(exClazz)));
		}
	}
}
