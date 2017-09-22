package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Supplier;

final class ProduceParser<T> extends ValParser<T>{
	private final Supplier<T> _func;
	ProduceParser(Supplier<T> func) {
		_func = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		return success(_func.get());
	}
}