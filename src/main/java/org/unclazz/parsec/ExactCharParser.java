package org.unclazz.parsec;

import java.io.IOException;

final class ExactCharParser extends Parser {
	private final char _ch;
	ExactCharParser(char ch) {
		_ch = ch;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final int ch = ctx.source().read();
		return ch == _ch ? success() 
				: failure("%s expected but %s found.", 
						ParsecUtility.charToString(_ch),
						ParsecUtility.charToString(ch));
	}
}
