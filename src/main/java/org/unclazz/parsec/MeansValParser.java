package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Supplier;

final class MeansValParser<T> extends ValParser<T> {
	private final Parser _original;
	private final Supplier<T> _supp;
	public MeansValParser(Parser original, Supplier<T> func) {
		ParsecUtility.mustNotBeNull("original", original);
		ParsecUtility.mustNotBeNull("func", func);
		_original = original;
		_supp = func;
	}
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		return _original.parse(ctx).attachValue(_supp.get());
	}
}
