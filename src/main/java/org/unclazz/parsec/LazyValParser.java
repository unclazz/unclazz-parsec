package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Supplier;

final class LazyValParser<T> extends ValParser<T>{
	private final Supplier<ValParser<T>> _func;
	private ValParser<T> _cache;
	LazyValParser(Supplier<ValParser<T>> func){
		_func = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.get();
		return _cache.doParse(ctx);
	}
}