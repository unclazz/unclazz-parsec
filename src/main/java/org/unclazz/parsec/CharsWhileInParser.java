package org.unclazz.parsec;

import java.io.IOException;

final class CharsWhileInParser extends Parser {
	private final CharClass _clazz;
	private final int _min;
	CharsWhileInParser(CharClass clazz, int min){
		ParsecUtility.mustNotBeNull("clazz", clazz);
		ParsecUtility.mustBeGreaterThanOrEqual("min", min, 0);
		_clazz = clazz;
		_min = min;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		int ch = -1, count = 0;
		while (0 <= (ch = src.peek()) && _clazz.contains(ch)) {
			src.read();
			count ++;
		}
		return _min <= count ? success() : failure("expected that length of char sequence is" +
                    " greater than or equal %s, but actualy it is %s.", _min, count);
	}
}