package org.unclazz.parsec;

import java.io.IOException;

final class UncaptureParser<T> extends Parser{
	private final ValParser<T> _original;
	UncaptureParser(ValParser<T> original) {
		_original = original;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		return _original.parse(ctx).detachValue();
	}
}