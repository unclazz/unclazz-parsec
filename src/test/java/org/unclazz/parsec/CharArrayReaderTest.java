package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class CharArrayReaderTest {

	@Test
	public void testFrom() {
		// Assert
		try {
			CharArrayReader.from("".toCharArray());
			CharArrayReader.from("abc".toCharArray());
		}catch (final Exception e) {
			fail();
		}
		try {
			CharArrayReader.from(null);
			fail();
		}catch (final Exception e) {
			// OK.
		}
	}

	@Test
	public void testHasReachedEof() {
		// Arrange
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());
		
		// Act
		// Assert
		assertThat(r_empty.hasReachedEof(), is(true));
		assertThat(r_empty.prepend("abc".toCharArray()).hasReachedEof(), is(false));
		assertThat(r_0123.hasReachedEof(), is(false)); r_0123.read(); // => 0
		assertThat(r_0123.hasReachedEof(), is(false)); r_0123.read(); // => 1
		assertThat(r_0123.hasReachedEof(), is(false)); r_0123.read(); // => 2
		assertThat(r_0123.hasReachedEof(), is(false)); r_0123.read(); // => 3
		assertThat(r_0123.hasReachedEof(), is(true)); r_0123.read(); // => -1
		assertThat(r_0123.hasReachedEof(), is(true));
	}

	@Test
	public void testSize() {
		// Arrange
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());
		
		// Act
		final int r_empty_size = r_empty.size();
		final int r_0123_size = r_0123.size();
		
		// Assert
		assertThat(r_empty_size, is(0)); // 空なら0
		assertThat(r_0123_size, is(4)); // それ以外なら内容に応じた長さ
		
		// Arrange 2
		r_0123.readToEnd(); // EOFまで読み進め
		
		// Act & Assert 2
		assertThat(r_0123.size(), is(0)); // EOFなので残りは0
		
		// Arrange & Act & Assert 3
		assertThat(r_empty.prepend("abc".toCharArray()).size(), is(3));
		assertThat(r_0123.prepend("abc".toCharArray()).size(), is(3));
	}

	@Test
	public void testRead() {
		// Arrange
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());
		final CharArrayReader r_abc = r_empty.prepend("abc".toCharArray());
		final CharArrayReader r_0123_2 = CharArrayReader.from("0123".toCharArray());
		r_0123_2.read(); r_0123_2.read();
		final CharArrayReader r_ab23 = r_0123_2.prepend("ab".toCharArray());
		
		// Act
		final int[] a_empty = new int[] { r_empty.read() };
		final int[] a_0123 = new int[] { r_0123.read(), r_0123.read(),
				r_0123.read(), r_0123.read(), r_0123.read() };
		final int[] a_abc = new int[] { r_abc.read(), r_abc.read(),
				r_abc.read(), r_abc.read(), r_abc.read() };
		final int[] a_ab23 = new int[] { r_ab23.read(), r_ab23.read(),
				r_ab23.read(), r_ab23.read(), r_ab23.read() };
		
		// Assert
		assertThat(a_empty, is(new int[] { -1 })); // 空のリーダーははじめからEOF（-1）を返す
		assertThat(a_0123, is(new int[] { '0', '1', '2', '3', -1 })); // 空でないものは文字位置の文字と最後にEOFを返す
		assertThat(a_abc, is(new int[] { 'a', 'b', 'c', -1, -1 })); // prependで先頭に要素追加すると再度文字を返すようになる
		assertThat(a_ab23, is(new int[] { 'a', 'b', '2', '3', -1 })); // 途中でprependしてもOK
	}

	@Test
	public void testReadToEnd() {
		// Arrange
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());
		final CharArrayReader r_0123_2 = CharArrayReader.from("0123".toCharArray());
		r_0123_2.readToEnd();
		final CharArrayReader r_0123_3 = r_0123_2.prepend("abc".toCharArray());
		final CharArrayReader r_XX23 = CharArrayReader.from("0123".toCharArray());
		r_XX23.read(); r_XX23.read();
		
		// Act
		final String s_empty = r_empty.readToEnd();
		final String s_0123 = r_0123.readToEnd();
		final String s_0123_2 = r_0123_2.readToEnd();
		final String s_0123_3 = r_0123_3.readToEnd();
		final String s_XX23 = r_XX23.readToEnd();
		
		// Assert
		assertThat(s_empty, is(nullValue())); // 空のリーダーは空文字列を返す
		assertThat(s_0123, is("0123")); // 正常系
		assertThat(s_0123_2, is(nullValue())); // EOFになったら空文字列
		assertThat(s_0123_3, is("abc")); // prepend後もOK
		assertThat(s_XX23, is("23")); // 読み取られるのはあくまでも文字位置以降
	}

	@Test
	public void testPeek() {
		// Arrange
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());

		// Act
		// Assert
		assertThat(r_empty.peek(), is(-1));
		
		assertThat(r_0123.peek(), is((int)'0'));
		assertThat(r_0123.peek(), is((int)'0'));
		assertThat(r_0123.read(), is((int)'0'));
		
		assertThat(r_0123.peek(), is((int)'1'));
		assertThat(r_0123.peek(), is((int)'1'));
		assertThat(r_0123.read(), is((int)'1'));

		assertThat(r_0123.peek(), is((int)'2'));
		assertThat(r_0123.peek(), is((int)'2'));
		assertThat(r_0123.read(), is((int)'2'));
		
		assertThat(r_0123.peek(), is((int)'3'));
		assertThat(r_0123.peek(), is((int)'3'));
		assertThat(r_0123.read(), is((int)'3'));
		
		assertThat(r_0123.peek(), is(-1));
		assertThat(r_0123.peek(), is(-1));
	}

	@Test
	public void testPrepend() {
		// Arrange
		// Act
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_abc_empty = r_empty.prepend("abc".toCharArray());
		final CharArrayReader r_0123 = CharArrayReader.from("0123".toCharArray());
		r_0123.readToEnd();
		final CharArrayReader r_abc_XXXX = r_0123.prepend("abc".toCharArray());
		final CharArrayReader r_XX23 = CharArrayReader.from("0123".toCharArray());
		r_XX23.read(); r_XX23.read();
		final CharArrayReader r_abc_XX23 = r_XX23.prepend("abc".toCharArray());
		final CharArrayReader r_xyz_abc_XX23 = r_abc_XX23.prepend("xyz".toCharArray());
		
		// Assert
		assertThat(r_abc_empty.readToEnd(), is("abc")); // 空のリーダーにprependも可能
		assertThat(r_abc_XXXX.readToEnd(), is("abc")); // EOF到達後のprependも可能
		assertThat(r_abc_XX23.readToEnd(), is("abc23")); // prepend以前に未読だったコンテンツは継承される
		assertThat(r_xyz_abc_XX23.readToEnd(), is("xyzabc23"));// 繰返しprependも可能
	}

	@Test
	public void testToArray() {
		final CharArrayReader r_empty = CharArrayReader.from("".toCharArray());
		final CharArrayReader r_abc_empty = r_empty.prepend("abc".toCharArray());
		
		assertThat(r_empty.toArray(), is(new char[0]));
		assertThat(r_abc_empty.toArray(), is(new char[] {'a', 'b', 'c'}));
	}

}
