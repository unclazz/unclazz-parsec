package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

public class TextReaderTest {

	@Test
	public void testFromInputStreamCharset_emptyTxt() {
		try (final TextReader tr = TextReader.from(getClass()
				.getResourceAsStream("test0_empty.txt"), Charset.forName("utf-8"))) {
			
			assertTrue(tr.noRemaining());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testFromInputStreamCharset_spacesTxt() {
		try (final TextReader tr = TextReader.from(getClass()
				.getResourceAsStream("test1_spaces.txt"), Charset.forName("utf-8"))) {
			
			assertTrue(tr.hasRemaining());
			assertTrue(tr.readToEnd().trim().length() == 0);
			assertTrue(tr.noRemaining());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testFromInputStreamCharset_abcTxt() {
		final ValParser<List<String>> spaceAlphabetic = 
				(Parsers.space().then(Parsers.charsWhileIn(CharClass.alphabetic(), 1).val())).rep()
				.then(Parsers.space()).then(Parsers.eof());
		
		try (final TextReader tr = TextReader.from(getClass()
				.getResourceAsStream("test2_abc.txt"), Charset.forName("utf-8"))) {
			
			assertTrue(tr.hasRemaining());
			
			final ValResult<List<String>> r = spaceAlphabetic.parse(tr);
			
			assertTrue(r.isSuccessful());
			assertTrue(tr.noRemaining());
			
			assertThat(r.value().get(0), is("abc"));
			assertThat(r.value().get(1), is("ABC"));
			assertThat(r.value().get(2), is("z"));
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
