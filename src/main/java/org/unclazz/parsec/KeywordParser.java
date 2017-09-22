package org.unclazz.parsec;

import java.io.IOException;

final class KeywordParser extends Parser {
	private final String _keyword;
	public KeywordParser(String keyword) {
		_keyword = keyword;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		for (int i = 0; i < _keyword.length(); i ++) {
			final char expected = _keyword.charAt(i);
			final int actual = ctx.source().read();
			if (expected != actual) {
				return failure("%s expected but %s found.", 
						ParsecUtility.charToString(expected),
						ParsecUtility.charToString(actual));
			}
		}
		return success();
	}
}