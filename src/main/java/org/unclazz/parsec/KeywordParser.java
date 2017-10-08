package org.unclazz.parsec;

import java.io.IOException;

final class KeywordParser extends Parser {
	private final String _keyword;
	private final int _cutIndex;
	KeywordParser(String keyword) {
		ParsecUtility.mustNotBeEmpty("keyword", keyword);
		_keyword = keyword;
		_cutIndex = -1;
	}
	KeywordParser(String keyword, int cutIndex) {
		ParsecUtility.mustNotBeEmpty("keyword", keyword);
		ParsecUtility.mustBeGreaterThanOrEqual("keyword", cutIndex, -1);
		_keyword = keyword;
		_cutIndex = cutIndex;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		for (int i = 0; i < _keyword.length(); i ++) {
			final char expected = _keyword.charAt(i);
			final int actual = src.peek();
			if (expected != actual) {
				final ResultCore rc = failure("%s expected but %s found.", 
						ParsecUtility.charToString(expected),
						ParsecUtility.charToString(actual));
				return _cutIndex == -1 || i < _cutIndex ? rc : rc.allowBacktrack(false);
			}
			src.read();
		}
		return success();
	}
}