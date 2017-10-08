package org.unclazz.parsec.sample;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.unclazz.parsec.sample.csv.Csv;
import org.unclazz.parsec.sample.csv.Row;

public class CsvParserTest {
	@Test
	public void testParseString() {
		try {
			final Csv parsed = Csv.parse("A1,\"\",\"C1\"\r\nA2,\"B2\",C2\n\"A3\",,C3\r\n");
			assertThat(parsed.size(), is(3));
			
			final Row row0 = parsed.get(0);
			assertThat(row0.size(), is(3));
			assertThat(row0.get(0).value(), is("A1"));
			assertThat(row0.get(0).isQuoted(), is(false));
			assertThat(row0.get(1).value(), is(""));
			assertThat(row0.get(1).isQuoted(), is(true));
			assertThat(row0.get(2).value(), is("C1"));
			assertThat(row0.get(2).isQuoted(), is(true));
			
			final Row row2 = parsed.get(2);
			assertThat(row2.size(), is(3));
			assertThat(row2.get(0).value(), is("A3"));
			assertThat(row2.get(0).isQuoted(), is(true));
			assertThat(row2.get(1).value(), is(""));
			assertThat(row2.get(1).isQuoted(), is(false));
			assertThat(row2.get(2).value(), is("C3"));
			assertThat(row2.get(2).isQuoted(), is(false));
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}
}
