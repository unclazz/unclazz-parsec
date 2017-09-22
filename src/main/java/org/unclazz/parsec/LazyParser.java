package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Supplier;

final class LazyParser extends Parser{
	private final Supplier<Parser> _func;
	private Parser _cache;
	LazyParser(Supplier<Parser> func){
		_func = func;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.get();
		return _cache.doParse(ctx);
	}
}