package org.unclazz.parsec;

import java.io.IOException;

final class CutValParser<T> extends ValParser<T> {
	private final ValParser<T> _original;
	CutValParser(ValParser<T> original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}
	@Override
	protected ValResult<T> doParse(Context ctx) throws IOException {
		final ValResult<T> r = _original.parse(ctx);
		return r.allowBacktrack(!r.isSuccessful());
	}
}