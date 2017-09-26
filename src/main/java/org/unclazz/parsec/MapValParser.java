package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Function;

final class MapValParser<T,U> extends ValParser<U> {
	private final ValParser<T> _original;
	private final Function<T, U> _func;
	private final boolean _canThrow;
	MapValParser(ValParser<T> original, Function<T, U> func, boolean canThrow) {
		ParsecUtility.mustNotBeNull("original", original);
		ParsecUtility.mustNotBeNull("func", func);
		_original = original;
		_func = func;
		_canThrow = canThrow;
	}

	@Override
	protected ValResultCore<U> doParse(Context ctx) throws IOException {
		try {
			return _original.parse(ctx).map(_func);
		} catch (final RuntimeException ex) {
			if (_canThrow) throw ex;
			else return failure("an error has occurred while mapping: %s ", ex.getMessage());
		}
	}
}
