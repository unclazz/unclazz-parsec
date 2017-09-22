package org.unclazz.parsec;

import java.io.IOException;

final class OptParser extends Parser {
	private final Parser _original;
	OptParser(Parser original) {
		_original = original;
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		final Result res = _original.parse(ctx);
		if (res.isSuccessful()) {
			src.unmark();
		}
		src.reset(true);
		return success();
	}
}