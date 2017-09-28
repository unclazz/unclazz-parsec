package org.unclazz.parsec;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.unclazz.parsec.data.Tuple;
import org.unclazz.parsec.data.Tuple2;

final class KeywordInParser extends Parser {
	private final List<Tuple2<String,Integer>> _entries;
	KeywordInParser(String[] keywords){
		ParsecUtility.mustNotBeNull("keywords", keywords);
		keywords = distinct(keywords);
		if (keywords.length == 0) throw new IllegalArgumentException("keywords must not be empty.");
		if (containsNullOrEmpty(keywords)) throw new IllegalArgumentException("keywords must not contain null or empty.");
		_entries = keywordAndCommonPrefixLength(keywords);
		_entries.add(Tuple.of(keywords[keywords.length - 1], -1));
		
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		Tuple2<String,Integer> entryCache = _entries.get(0);
		
		for (int i = 0, j = 0; i < entryCache.item1().length() && src.hasRemaining();) {
			final char actual = (char) src.peek();
			final char expected = entryCache.item1().charAt(i);
			
			if (actual == expected) {
				src.read(); 
				i++;
			} else if (i <= entryCache.item2()) {
				entryCache = _entries.get(++ j);
			} else {
				return failure("%s expected but %s found.",
						ParsecUtility.charToString(expected),
						ParsecUtility.charToString(actual));
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
			buf.add(Tuple.of(k0, commonPrefixLength(k0, k1)));
		}
		return buf;
	}
}