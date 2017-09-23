package org.unclazz.parsec;

import java.io.IOException;

final class CutParser extends Parser {
	private final Parser _original;
	CutParser(Parser original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final Result r = _original.parse(ctx);
		return r.allowBacktrack(!r.isSuccessful());
	}
}