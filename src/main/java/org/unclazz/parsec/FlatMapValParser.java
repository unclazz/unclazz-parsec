package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Function;

final class FlatMapValParser<T,U> extends ValParser<U> {
	private final ValParser<T> _original;
	private final Function<T, ValParser<U>> _func;
	FlatMapValParser(ValParser<T> original, Function<T, ValParser<U>> func) {
		ParsecUtility.mustNotBeNull("original", original);
		ParsecUtility.mustNotBeNull("func", func);
		_original = original;
		_func = func;
	}

	@Override
	protected ValResultCore<U> doParse(Context ctx) throws IOException {
		final ValResult<T> r = _original.parse(ctx);
		return r.isSuccessful() 
				? _func.apply(r.value()).parse(ctx)
						: failure(r.message());
	}
}