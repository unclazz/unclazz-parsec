package org.unclazz.parsec;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.parsec.data.Tuple2;

final class KeywordInParser extends Parser {
	private final Parser _inner;
	KeywordInParser(String[] keywords){
		ParsecUtility.mustNotBeNull("keywords", keywords);
		keywords = distinct(keywords);
		if (keywords.length == 0) throw new IllegalArgumentException("keywords must not be empty.");
		if (containsNullOrEmpty(keywords)) throw new IllegalArgumentException("keywords must not contain null or empty.");
		_inner = compose(keywords);
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		return _inner.parse(ctx);
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
		Arrays.sort(ks);
		final String[] tmp = new String[ks.length];
		int size = 0;
		for (int i = size; i < ks.length; i ++) {
			final String k = ks[i];
			if (Arrays.binarySearch(tmp, k) != -1) {
				ks[size ++] = k;
			}
		}
		return Arrays.copyOf(tmp, size);
	}
	private static Parser compose(String[] ks) {
        // キーワード数が1の場合、単にKeywordParserのインスタンスを返すだけ
		if (ks.length == 1) return new KeywordParser(ks[0]);
		
        // 合成過程のパーサーを格納する一時変数
		Parser tmp = null;

        // キーワードのコレクションをそれ自身（ただし最初の要素はスキップ）とZIPして、
        // {キーワード, 隣接するキーワードとの共通接頭辞の長さ}
        // という2メンバーを持つ一時型インスタンスのシーケンスに変換
		final List<Tuple2<String,Integer>> zip = keywordAndCommonPrefixLength(ks);
		
        // ZIPした結果に基づきループを行いパーサーを段階構築
		for (final Tuple2<String,Integer> pair : zip) {
            // キーワードの長さ と 共通接頭辞の長さ＋1 のいずれか小さい方をカット（Cut）の文字位置として採用
            // ＊共通接頭辞の長さに＋1をすると、キーワードの長さをオーバーする可能性がある。
            // これはKeywordParserのコンストラクタ引数の値として不正とみなされるので、
            // キーワード自体の長さと比較を行い短い方を採用する。
            final int cutIndex = Math.min(pair.item1().length(), pair.item2() + 1);
			
            // キーワードを単体で読み取るパーサーを作成
            final Parser nextParser = new KeywordParser(pair.item1(), cutIndex);
            
            // 一時変数が空 ＝ 初回 は、作成したパーサーを単純にアサインする
            // 一時変数が空でない ＝ 2回目以降 は、Orで連結して再アサインする
            tmp = tmp == null ? nextParser : tmp.or(nextParser);
		}
        // 終わりに、キーワードのコレクションの末尾のものをOrで連結する
        // ＊ZIPを行った時、末尾のキーワードは除外されてしまうため、ここで救済する
        return tmp.or(new KeywordParser(ks[ks.length - 1]));
	}
	private static int commonPrefixLength(String k0, String k1) {
		final int loopCount = Math.min(k0.length(), k1.length());
		for (int i = 0; i < loopCount; i ++) {
			if (k0.charAt(i) != k1.charAt(i)) return i;
		}
		return loopCount;
	}
	private static List<Tuple2<String,Integer>> keywordAndCommonPrefixLength(String[] ks) {
		final List<Tuple2<String,Integer>> buf = new LinkedList<>();
		for (int i = 0; i < ks.length - 1; i ++) {
			final String k0 = ks[i];
			final String k1 = ks[i + 1];
			buf.add(Tuple2.of(k0, commonPrefixLength(k0, k1)));
		}
		return buf;
	}
}