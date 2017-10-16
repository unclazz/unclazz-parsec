package org.unclazz.parsec;

import java.io.IOException;

final class SkipToParser extends Parser {
	private final Parser _token;
	SkipToParser(Parser token) {
		ParsecUtility.mustNotBeNull("token", token);
		_token = token;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		while (src.hasRemaining()) {
			final Result res = _token.parse(ctx);
			if (res.isSuccessful()) return res;
			src.read();
		}
		return failure("expected token not found.");
	}
}
