package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.ValParserFactory;

final class LazyValParser<T> extends ValParser<T>{
	private final ValParserFactory<T> _func;
	private ValParser<T> _cache;
	LazyValParser(ValParserFactory<T> func){
		ParsecUtility.mustNotBeNull("func", func);
		_func = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = _func.create();
		return _cache.doParse(ctx);
	}
}