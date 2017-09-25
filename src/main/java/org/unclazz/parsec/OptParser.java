package org.unclazz.parsec;

import java.io.IOException;

final class OptParser extends Parser {
	private final Parser _original;
	OptParser(Parser original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		final Result res = _original.parse(ctx);
		if (res.isSuccessful()) {
			src.unmark();
			return success();
		}
		src.reset(true);
		return success();
	}
}