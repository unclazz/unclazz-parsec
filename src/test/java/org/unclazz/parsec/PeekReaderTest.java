package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class PeekReaderTest {

	@Test
	public void testPeek() throws IOException {
		// Arrange
		try (final PeekReader r = new PeekReader(new StringReader("012"))){
			// Act
			// Assert
			assertThat(r.peek(), is('0' - 0));
			r.read();
			assertThat(r.peek(), is('1' - 0));
			r.read();
			assertThat(r.peek(), is('2' - 0));
			r.read();
			assertThat(r.peek(), is(-1));
		}
	}

	@Test
	public void testRead() throws IOException {
		// Arrange
		try (final PeekReader r = new PeekReader(new StringReader("012"))){
			// Act
			// Assert
			assertThat(r.read(), is('0' - 0));
			assertThat(r.read(), is('1' - 0));
			assertThat(r.read(), is('2' - 0));
			assertThat(r.read(), is(-1));
		}
	}

}
