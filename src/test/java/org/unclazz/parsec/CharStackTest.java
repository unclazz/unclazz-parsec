package org.unclazz.parsec;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class CharStackTest {

	@Test
	public void testCharStack() {
		try {
			new CharStack(0);
			fail();
		}catch (Exception e) {
			// OK
		}
		try {
			new CharStack(1);
			assertThat(new CharStack(1).size(), is(0));
			// OK
		}catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testPush() {
		// Arrange
		final CharStack s = new CharStack(1);;
		
		// Act
		s.push('a');
		
		// Assert
		assertThat(s.size(),is(1));
		
		// Act
		s.push('b');
		
		// Assert
		assertThat(s.size(),is(2));
		assertThat(s.toString(),is("ab"));
	}

	@Test
	public void testPop() {
		// Arrange
		final CharStack s = new CharStack(1);;
		s.push('0');
		s.push('1');
		s.push('2');
		s.push('3');
		
		// Act
		final char[] p = s.pop(2);
		
		// Assert
		assertThat(p,is(new char[] {'2', '3'}));
		assertThat(s.size(),is(2));
		assertThat(s.toString(),is("01"));
	}

	@Test
	public void testPopAll() {
		// Arrange
		final CharStack s = new CharStack(1);;
		s.push('0');
		s.push('1');
		s.push('2');
		s.push('3');
		
		// Act
		final char[] p = s.popAll();
		
		// Assert
		assertThat(p,is("0123".toCharArray()));
		assertThat(s.size(),is(0));
		assertThat(s.toString(),is(""));
	}

	@Test
	public void testSize() {
		final CharStack s = new CharStack(1);;

		assertThat(s.size(), is(0)); s.push('0');
		assertThat(s.size(), is(1)); s.push('1');
		assertThat(s.size(), is(2)); s.push('2');
		assertThat(s.size(), is(3)); s.push('3');
		assertThat(s.size(), is(4)); 
		
		s.popAll();
		
		assertThat(s.size(),is(0));
	}

	@Test
	public void testToArrayInt() {
		final CharStack s = new CharStack(1);
		s.push('0');
		s.push('1');
		s.push('2');
		assertThat(s.toArray(1), is(new char[] { '1', '2' }));
		assertThat(s.toArray(2), is(new char[] { '2' }));
		assertThat(s.toArray(3), is(new char[0]));
	}

	@Test
	public void testToArray() {
		final CharStack s = new CharStack(1);

		assertThat(s.toArray(), is(new char[0])); s.push('0');
		assertThat(s.toArray(), is(new char[] { '0' })); s.push('1');
		assertThat(s.toArray(), is(new char[] { '0', '1' })); s.push('2');
		assertThat(s.toArray(), is(new char[] { '0', '1', '2' })); s.push('3');
		
		s.popAll();
		
		assertThat(s.toArray(), is(new char[0]));
	}

	@Test
	public void testToStringInt() {
		final CharStack s = new CharStack(1);;

		s.push('0');
		s.push('1');
		assertThat(s.toString(0), is("01"));
		assertThat(s.toString(1), is("1"));
		assertThat(s.toString(2), is(""));
		s.push('2');
		assertThat(s.toString(0), is("012"));
		assertThat(s.toString(1), is("12"));
		assertThat(s.toString(2), is("2"));
		s.push('3');
		assertThat(s.toString(0), is("0123"));
		assertThat(s.toString(1), is("123"));
		assertThat(s.toString(2), is("23"));
	}

	@Test
	public void testToString() {
		final CharStack s = new CharStack(1);;

		assertThat(s.toString(), is(""));
		s.push('0');
		assertThat(s.toString(), is("0"));
		s.push('1');
		assertThat(s.toString(), is("01"));
		s.push('2');
		assertThat(s.toString(), is("012"));
		s.push('3');
		assertThat(s.toString(), is("0123"));
		
	}

	@Test
	public void testClear() {
		final CharStack s = new CharStack(1);;

		s.push('0');
		s.push('1');
		s.push('2');
		s.push('3');
		
		s.clear();
		
		assertThat(s.size(),is(0));
	}

}
