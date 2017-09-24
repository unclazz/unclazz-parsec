package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;

final class OptValParser<T> extends ValParser<Optional<T>> {
	private final ValParser<T> _original;
	OptValParser(ValParser<T> original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}

	@Override
	protected ValResultCore<Optional<T>> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		final ValResult<T> res = _original.parse(ctx);
		if (res.isSuccessful()) {
			src.unmark();
			return res.map(Optional::of);
		}
		src.reset(true);
		return success(Optional.empty());
	}
}