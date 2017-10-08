package org.unclazz.parsec;

import java.io.IOException;

final class ExactCharParser extends Parser {
	private final char _ch;
	ExactCharParser(char ch) {
		_ch = ch;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		final int ch = src.peek();
		if (ch == _ch) {
			src.read();
			return success();
		}
		return failure("%s expected but %s found.", 
						ParsecUtility.charToString(_ch),
						ParsecUtility.charToString(ch));
	}
}
