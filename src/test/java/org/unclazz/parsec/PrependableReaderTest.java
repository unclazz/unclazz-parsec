package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class PrependableReaderTest {
	
	private PrependableReader create(String text) {
		return new PrependableReader(new StringReader(text));
	}

	@Test
	public void testPeek() throws IOException {
		// Arrange
		final PrependableReader r = create("012");
		
		// Act
		// Assert
		assertThat(r.peek(), is('0' - 0));
		r.read();
		assertThat(r.peek(), is('1' - 0));
		r.read();
		assertThat(r.peek(), is('2' - 0));
		r.read();
		assertThat(r.peek(), is(-1));
		
		r.reattach(CharPosition.ofBof(), "abc".toCharArray());
		
		assertThat(r.peek(), is('a' - 0));
		r.read();
		assertThat(r.peek(), is('b' - 0));
		r.read();
		assertThat(r.peek(), is('c' - 0));
		r.read();
		assertThat(r.peek(), is(-1));
		
		r.reattach(CharPosition.ofBof(), "ABC".toCharArray());
		
		assertThat(r.peek(), is('A' - 0));
		r.read();
		assertThat(r.peek(), is('B' - 0));
		r.read();
		
		r.reattach(CharPosition.ofBof(), "XYZ".toCharArray());
		
		assertThat(r.peek(), is('X' - 0));
		r.read();
		assertThat(r.peek(), is('Y' - 0));
		r.read();
		assertThat(r.peek(), is('Z' - 0));
		r.read();
		assertThat(r.peek(), is('C' - 0));
		r.read();
		assertThat(r.peek(), is(-1));
	}

	@Test
	public void testPrependableReaderCharPositionCharArrayReader() throws IOException {
		try(final PrependableReader r = new PrependableReader(CharPosition.ofBof(), null, new StringReader("abc"))) {
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
		try(final PrependableReader r = new PrependableReader(null, "012".toCharArray(), new StringReader("abc"))) {
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testPrependableReaderReader() throws IOException {
		try(final PrependableReader r = new PrependableReader(null)) {
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public void testReattach() throws IOException {
		// Arrange
		final PrependableReader r = create("012");
		
		// Act
		// Assert
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		assertThat(r.read(), is('2' - 0));
		assertThat(r.read(), is(-1));
		
		r.reattach(CharPosition.ofBof(), "abc".toCharArray());
		
		assertThat(r.read(), is('a' - 0));
		assertThat(r.read(), is('b' - 0));
		assertThat(r.read(), is('c' - 0));
		assertThat(r.read(), is(-1));
		
		r.reattach(CharPosition.ofBof(), "ABC".toCharArray());
		
		assertThat(r.read(), is('A' - 0));
		assertThat(r.read(), is('B' - 0));
		
		r.reattach(CharPosition.ofBof(), "XYZ".toCharArray());
		
		assertThat(r.read(), is('X' - 0));
		assertThat(r.read(), is('Y' - 0));
		assertThat(r.read(), is('Z' - 0));
		assertThat(r.read(), is('C' - 0));
		assertThat(r.read(), is(-1));
	}

}
