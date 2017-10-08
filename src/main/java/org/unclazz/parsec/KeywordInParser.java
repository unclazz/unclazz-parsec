package org.unclazz.parsec;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

final class KeywordInParser extends Parser {
	private final Entry[] _entries;
	KeywordInParser(String[] keywords){
		ParsecUtility.mustNotBeNull("keywords", keywords);
		if (keywords.length == 0) throw new IllegalArgumentException("keywords must not be empty.");
		if (containsNullOrEmpty(keywords)) throw new IllegalArgumentException("keywords must not contain null or empty.");
		
		// キーワードをソートし かつ 重複を除去する
		final String[] uniqAndSorteds = distinct(keywords);
		if (keywords.length != uniqAndSorteds.length) throw new IllegalArgumentException("duplicated keywords found.");
		
		// キーワードチェックに用いるエントリーのリストを作成
		_entries = makeEntries(uniqAndSorteds);
		
		param("keywords", uniqAndSorteds);
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		Entry entryCache = _entries[0];
		
		// 文字位置（i）とキーワードエントリーID（j）をループ変数に
		// 現在キーワード（エントリー）の文字長とデータソースの残存コンテンツを条件にして、ループを実施
		for (int i = 0, j = 0; i < entryCache.keyword.length() && src.hasRemaining();) {
			// データソースから現在文字位置の文字を取得
			final int actual = src.peek();
			// 現在キーワード（エントリー）の同じ文字位置の文字を取得
			final char expected = entryCache.keyword.charAt(i);
			
			// 比較を行う
			if (actual == expected) {
				// 同一である場合
				// データソースの文字位置を前進させ、
				// 次の文字のチェックのため文字位置（i）も前進させる
				src.read(); 
				i++;
			} else if (i == entryCache.forkIndex) {
				// 文字は同一でないが、
				// 文字位置（i）が現在キーワードと次キーワードの内容との分岐位置とが一致する場合
				// 次キーワードの残りのコンテンツと入力データソースの残りのコンテンツのマッチを試みるため、
				// 比較対称のキーワードエントリーIDを次のものに変更
				entryCache = _entries[++ j];
			} else {
				// それ以外の場合
				while (true) {
					// 文字位置（i）と現在キーワードのすぐ次のキーワード内容との分岐位置（forkIndex）を比較
					if (i < entryCache.forkIndex) {
						// ［条件A］次キーワード内容との分岐位置より現在文字位置の方が小さい
						// つまり、次キーワードの現在文字位置の文字は現キーワードのそれと同じ
						// つまり、かならず比較結果はNGとなる
						// つまり、次のキーワードをチェックしても無駄
						// さらに次のキーワードを確認するためjをインクリメントだけする
						entryCache = _entries[++ j];
					} else if (entryCache.forkIndex < i) {
						// ［条件B］次キーワード内容との分岐位置より現在文字位置の方が大きい
						// つまり、次キーワードの先頭から現在文字位置までのシーケンスは、
						// 現キーワードのそれ（少なくとも現在文字位置までは入力データソースのシーケンスと一致した）とは異なる
						// つまり、入力データソースのシーケンスと比較した場合、かならずNGとなる
						// これ以上の比較は無駄なのでパース失敗を示す値を返す
						return failure("%s expected but %s found.",
								ParsecUtility.charToString(expected),
								ParsecUtility.charToString(actual));
					} else {
						// ［条件C］次キーワード内容との分岐位置が現在文字位置と同じ
						// つまり、次キーワードの現在文字位置の文字は現キーワードのそれと異なる
						// つまり、比較結果はOKとなる可能性がある（NGとなる可能性もある）
						// つまり、チェックしてみる価値がある
						// 次キーワードを使い比較を行うためjをインクリメントし、この内側ループを抜ける
						entryCache = _entries[++ j];
						break;
					}
				}
			}
		}
		
		return success();
	}
	private static boolean containsNullOrEmpty(String[] ks) {
		for (int i = 0; i < ks.length; i ++) {
			final String k = ks[i];
			if (k == null || k.length() == 0) return true;
		}
		return false;
	}
	private static String[] distinct(String[] ks) {
		if (ks.length <= 1) return ks;
		return new TreeSet<>(Arrays.asList(ks)).toArray(new String[0]);
	}
	private static Entry[] makeEntries(String[] ks) {
		final List<Entry> buf = new LinkedList<>();
		for (int i = 0; i < ks.length - 1; i ++) {
			final String k0 = ks[i];
			final String k1 = ks[i + 1];
			buf.add(new Entry(k0, forkIndex(k0, k1)));
		}
		buf.add(new Entry(ks[ks.length - 1], -1));
		return buf.toArray(new Entry[buf.size()]);
	}
	private static int forkIndex(String k0, String k1) {
		final int loopCount = Math.min(k0.length(), k1.length());
		for (int i = 0; i < loopCount; i ++) {
			if (k0.charAt(i) != k1.charAt(i)) return i;
		}
		return loopCount;
	}
	
	static final class Entry {
		public final String keyword;
		public final int forkIndex;
		Entry(String keyword, int forkIndex){
			this.keyword = keyword;
			this.forkIndex = forkIndex;
		}
	}
}