package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;

final class OrOptParser<T> extends ValParser<Optional<T>>{
	private final Parser _left;
	private final ValParser<T> _right;
	
	protected OrOptParser(Parser left, ValParser<T> right) {
		_left = left;
		_right = right;
	}
	
	@Override
	protected ValResultCore<Optional<T>> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		
		final Result leftResult = _left.parse(ctx);
		if (leftResult.isSuccessful() || !leftResult.canBacktrack()) {
			src.unmark();
			return success(Optional.empty()).allowBacktrack(true);
		}
		
		src.reset(true);
		final ValResult<T> rightResult = _right.parse(ctx);
		return rightResult.map(a->Optional.of(a)).allowBacktrack(true);
	}
}