package org.unclazz.parsec;

import java.io.IOException;

final class EofParser extends Parser {
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final int ch = ctx.source().peek();
		return ch == -1 ? success() : failure("EOF expected but %s found."
						, ParsecUtility.charToString(ch));
	}
}