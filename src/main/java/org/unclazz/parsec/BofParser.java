package org.unclazz.parsec;

import java.io.IOException;

final class BofParser extends Parser {
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		return src.position().index() == 0 
				? success() : failure("BOF expected but %s found."
						, ParsecUtility.charToString(src.peek()));
	}
}