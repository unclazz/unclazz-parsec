package org.unclazz.parsec;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class MappersTest {

	@Test
	public void testJavaString() {
		assertThat(Mappers.javaString("abc"), is("abc"));
		assertThat(Mappers.javaString("\\r\\n"), is("\r\n"));
		assertThat(Mappers.javaString("a\\rb\\nc"), is("a\rb\nc"));
		assertThat(Mappers.javaString("\\\\\\\""), is("\\\""));
		assertThat(Mappers.javaString("\\u0041\\u0061"), is("Aa"));
		assertThat(Mappers.javaString("\\061\\61\\101\\141"), is("11Aa"));
	}
	
	@Test
	public void testDigits() {
		assertThat(Mappers.digits("1234"), is(1234));
		assertThat(Mappers.digits("-1234"), is(-1234));
		assertThat(Mappers.floatingPoint("-12.34"), is(-12.34));
	}
	
	@Test
	public void testFloatingPoint() {
		assertThat(Mappers.floatingPoint("-12.34"), is(-12.34));
	}
}

