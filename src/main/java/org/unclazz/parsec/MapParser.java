package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Function;

final class MapParser<T,U> extends ValParser<U> {
	private final ValParser<T> _original;
	private final Function<T, U> _func;
	MapParser(ValParser<T> original, Function<T, U> func) {
		super("Map");
		_original = original;
		_func = func;
	}

	@Override
	protected ValResultCore<U> doParse(Context ctx) throws IOException {
		return _original.parse(ctx).map(_func);
	}
}
