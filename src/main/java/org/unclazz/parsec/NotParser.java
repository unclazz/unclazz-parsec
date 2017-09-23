package org.unclazz.parsec;

import java.io.IOException;

final class NotParser extends Parser{
	private final Parser _original;
	NotParser(Parser original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		return _original.parse(ctx).isSuccessful() 
				? failure("invalid token found.") : success();
	}
}