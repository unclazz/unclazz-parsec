package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ResettableReaderTest {

	private ResettableReader create(String text) {
		return new ResettableReader(new StringReader(text));
	}
	
	@Test
	public final void testMark() throws IOException {
		final ResettableReader r = create("01234");
		
		// 1度目のマーク
		r.mark();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// リセットすると1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// 何度リセットしても1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));

		// 2度目のマーク
		r.mark();
		assertThat(r.read(), is('1' - 0));
		assertThat(r.read(), is('2' - 0));
		
		// リセットすると2度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('1' - 0));
		
		// マークを解除してもリセットはかからない
		r.unmark();
		assertThat(r.read(), is('2' - 0));
		
		// 2度目のマークを解除したのでリセットで1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
	}

	@Test
	public final void testUnmark() throws IOException {
		final ResettableReader r = create("01234");
		
		// 空振りのマーク解除オペ（何も起きない）
		r.unmark();
		
		// 1度目のマーク
		r.mark();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// マークを解除してもリセットはかからない
		r.unmark();
		assertThat(r.read(), is('2' - 0));
		
		// マークを解除したのでリセットは空振りする
		r.reset();
		assertThat(r.read(), is('3' - 0));
		assertThat(r.read(), is('4' - 0));
	}

	@Test
	public final void testCaptureBoolean() throws IOException {
		final ResettableReader r = create("01234");
		
		assertThat(r.capture(true), is(nullValue()));
		
		// 1度目のマーク
		r.mark();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		assertThat(r.capture(true), is("01"));
		
		// マークは解除されておりリセットは空振りする
		r.reset();
		assertThat(r.read(), is('2' - 0));
	}

	@Test
	public final void testCapture() throws IOException {
		final ResettableReader r = create("01234");
		
		assertThat(r.capture(), is(nullValue()));
		
		// 1度目のマーク
		r.mark();
		assertThat(r.capture(), is(""));
		assertThat(r.read(), is('0' - 0));
		assertThat(r.capture(), is("0"));
		assertThat(r.read(), is('1' - 0));
		assertThat(r.capture(), is("01"));
		
		// リセットすると1度目のマークに戻る
		r.reset();
		assertThat(r.capture(), is(""));
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		assertThat(r.capture(), is("01"));
		
		// 何度リセットしても1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.capture(), is("0"));

		// 2度目のマーク
		r.mark();
		assertThat(r.read(), is('1' - 0));
		assertThat(r.read(), is('2' - 0));
		assertThat(r.capture(), is("12"));
		
		// リセットすると2度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('1' - 0));
		
		// マークを解除してもリセットはかからない、しかしキャプチャ結果には影響する
		r.unmark();
		assertThat(r.read(), is('2' - 0));
		assertThat(r.capture(), is("012"));
	}

	@Test
	public final void testReset() throws IOException {
		final ResettableReader r = create("01234");
		
		// 空振りのリセットオペ（何も起きない）
		r.reset();
		
		// 1度目のマーク
		r.mark();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// リセットすると1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// 何度リセットしても1度目のマークに戻る
		r.reset();
		r.reset();
		r.reset();
		assertThat(r.read(), is('0' - 0));

		// 2度目のマーク
		r.mark();
		assertThat(r.read(), is('1' - 0));
		assertThat(r.read(), is('2' - 0));
		
		// リセットすると2度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('1' - 0));
		
		// マークを解除してもリセットはかからない
		r.unmark();
		assertThat(r.read(), is('2' - 0));
		
		// 2度目のマークを解除したのでリセットで1度目のマークに戻る
		r.reset();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
	}

	@Test
	public final void testResetBoolean() throws IOException {
		final ResettableReader r = create("01234");
		
		// 空振りのリセットオペ（何も起きない）
		r.reset(true);
		
		// 1度目のマーク
		r.mark();
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// リセットすると1度目のマークに戻る
		r.reset(true);
		assertThat(r.read(), is('0' - 0));
		assertThat(r.read(), is('1' - 0));
		
		// すでにマークは解除されているのでリセットは空振り
		r.reset();
		assertThat(r.read(), is('2' - 0));
		assertThat(r.read(), is('3' - 0));
	}

}
