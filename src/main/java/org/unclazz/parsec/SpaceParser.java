package org.unclazz.parsec;

import java.io.IOException;

final class SpaceParser extends Parser {
	private final int _min;
	SpaceParser(int min) {
		ParsecUtility.mustBeGreaterThanOrEqual("min", min, 0);
		_min = min;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		int ch = -1, count = 0;
		while (0 <= (ch = src.peek()) && ch <= 32) {
			src.read();
			count ++;
		}
		return _min <= count ? success() : failure("%s space(s) found "
				+ "but more space(s) needed (min=%s).", count, _min);
	}	
}
