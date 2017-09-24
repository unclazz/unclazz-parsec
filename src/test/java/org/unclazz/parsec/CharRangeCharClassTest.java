package org.unclazz.parsec;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Field;
import java.nio.CharBuffer;

import org.junit.Test;

public class CharRangeCharClassTest {

	@Test
	public final void testUnion() {
		final CharClass clz_2_4 = CharClass.between('2', '4');
		final CharClass clz_2_3 = CharClass.between('2', '3'); // clz_2_4に包含される
		final CharClass clz_3_4 = CharClass.between('3', '4'); // clz_2_4に包含される
		final CharClass clz_3_6 = CharClass.between('3', '6'); // clz_2_4と重なる
		final CharClass clz_5_7 = CharClass.between('5', '7'); // clz_2_4と隣接する
		final CharClass clz_6_8 = CharClass.between('6', '8'); // clz_2_4と隣接しない
		
		final CharClass clz_2_4_union_clz_2_3 = clz_2_4.union(clz_2_3);// 包含
		final CharClass clz_2_4_union_clz_2_4 = clz_2_4.union(clz_2_4);// 同一
		final CharClass clz_2_4_union_clz_3_4 = clz_2_4.union(clz_3_4);// 包含
		final CharClass clz_2_4_union_clz_3_6 = clz_2_4.union(clz_3_6);// 重なる
		final CharClass clz_2_4_union_clz_5_7 = clz_2_4.union(clz_5_7);// 隣接
		final CharClass clz_2_4_union_clz_6_8 = clz_2_4.union(clz_6_8);// 隔絶
		
		// 合成結果をcontainsの挙動から確認
		assertThat(checkEach(clz_2_4, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_union_clz_2_3, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_union_clz_2_4, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_union_clz_3_4, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_union_clz_3_6, "0123456789X"), is("00111110000"));
		assertThat(checkEach(clz_2_4_union_clz_5_7, "0123456789X"), is("00111111000"));
		assertThat(checkEach(clz_2_4_union_clz_6_8, "0123456789X"), is("00111011100"));
		
		final CharRange[] clz_2_4_rngs = privateRanges(clz_2_4);
		final CharRange[] clz_2_4_union_clz_2_3_rngs = privateRanges(clz_2_4_union_clz_2_3);// 包含
		final CharRange[] clz_2_4_union_clz_2_4_rngs = privateRanges(clz_2_4_union_clz_2_4);// 同一
		final CharRange[] clz_2_4_union_clz_3_4_rngs = privateRanges(clz_2_4_union_clz_3_4);// 包含
		final CharRange[] clz_2_4_union_clz_3_6_rngs = privateRanges(clz_2_4_union_clz_3_6);// 重なる
		final CharRange[] clz_2_4_union_clz_5_7_rngs = privateRanges(clz_2_4_union_clz_5_7);// 隣接
		final CharRange[] clz_2_4_union_clz_6_8_rngs = privateRanges(clz_2_4_union_clz_6_8);// 隔絶
		
		// 合成結果の文字範囲が最適化されていることを確認
		assertThat(clz_2_4_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_2_3_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_2_4_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_3_4_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_3_6_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_5_7_rngs.length, is(1));
		assertThat(clz_2_4_union_clz_6_8_rngs.length, is(2));
	}

	@Test
	public final void testPlus() {
		final CharClass clz_2_4 = CharClass.between('2', '4');
		final CharClass clz_2_4_plus_3 = clz_2_4.plus('3'); // 内包
		final CharClass clz_2_4_plus_4 = clz_2_4.plus('4'); // 内包
		final CharClass clz_2_4_plus_5 = clz_2_4.plus('5'); // 隣接
		final CharClass clz_2_4_plus_6 = clz_2_4.plus('6'); // 独立
		
		// 合成結果をcontainsの挙動から確認
		assertThat(checkEach(clz_2_4, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_plus_3, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_plus_4, "0123456789X"), is("00111000000"));
		assertThat(checkEach(clz_2_4_plus_5, "0123456789X"), is("00111100000"));
		assertThat(checkEach(clz_2_4_plus_6, "0123456789X"), is("00111010000"));
		
		final CharRange[] clz_2_4_rngs = privateRanges(clz_2_4);
		final CharRange[] clz_2_4_plus_3_rngs = privateRanges(clz_2_4_plus_3);
		final CharRange[] clz_2_4_plus_4_rngs = privateRanges(clz_2_4_plus_4);
		final CharRange[] clz_2_4_plus_5_rngs = privateRanges(clz_2_4_plus_5);
		
		// 合成結果の文字範囲が最適化されていることを確認
		assertThat(clz_2_4_rngs.length, is(1));
		assertThat(clz_2_4_plus_3_rngs.length, is(1));
		assertThat(clz_2_4_plus_4_rngs.length, is(1));
		assertThat(clz_2_4_plus_5_rngs.length, is(1));
		assertThat(clz_2_4_plus_6 instanceof UnionCharClass, is(true));
	}
	
	private String checkEach(CharClass clz, String cs) {
		final CharBuffer buf = CharBuffer.allocate(cs.length());
		for (final char c : cs.toCharArray()) {
			buf.put(clz.contains(c) ? '1' : '0');
		}
		return buf.flip().toString();
	}
	
	private CharRange[] privateRanges(CharClass clz) {
		try {
			final Field f = clz.getClass().getDeclaredField("_charRanges");
			f.setAccessible(true);
			final Object o = f.get(clz);
			return (CharRange[]) o;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
