package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Supplier;

final class ProductParser<T> extends ValParser<T>{
	private final Supplier<T> _func;
	ProductParser(Supplier<T> func) {
		super("Yield");
		_func = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		return success(_func.get());
	}
}