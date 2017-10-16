package org.unclazz.parsec;

import java.io.IOException;

final class SkipToValParser<T> extends ValParser<T> {
	private final ValParser<T> _token;
	SkipToValParser(ValParser<T> token) {
		ParsecUtility.mustNotBeNull("token", token);
		_token = token;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		while (src.hasRemaining()) {
			final ValResult<T> res = _token.parse(ctx);
			if (res.isSuccessful()) return res;
			src.read();
		}
		return failure("expected token not found.");
	}
}
