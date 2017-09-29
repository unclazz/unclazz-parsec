package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.ParserFactory;

final class LazyParser extends Parser{
	private final ParserFactory _func;
	private Parser _cache;
	LazyParser(ParserFactory func){
		ParsecUtility.mustNotBeNull("func", func);
		_func = func;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.create();
		return _cache.doParse(ctx);
	}
}