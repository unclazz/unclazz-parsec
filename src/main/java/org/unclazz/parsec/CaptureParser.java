package org.unclazz.parsec;

import java.io.IOException;

final class CaptureParser extends ValParser<String> {
	private final Parser _original;
	
	CaptureParser(Parser original) {
		_original = original;
	}

	@Override
	protected ValResultCore<String> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		final Result res = _original.parse(ctx);
		if (res.isSuccessful()) {
			return res.attachValue(src.capture(true));
		}
		src.unmark();
		return res.attachValue();
	}
}
