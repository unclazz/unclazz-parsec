package org.unclazz.parsec;

import java.io.IOException;

final class LookaheadParser extends Parser{
	private final Parser _original;
	
	LookaheadParser(Parser original) {
		_original = original;
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		final Result r = _original.parse(ctx);
		src.reset(true);
		return r;
	}
}