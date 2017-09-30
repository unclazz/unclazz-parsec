package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;

final class OptOrParser<T> extends ValParser<Optional<T>>{
	private final ValParser<T> _left;
	private final Parser _right;
	
	protected OptOrParser(ValParser<T> left, Parser right) {
		_left = left;
		_right = right;
	}
	
	@Override
	protected ValResultCore<Optional<T>> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		
		final ValResult<T> leftResult = _left.parse(ctx);
		if (leftResult.isSuccessful() || !leftResult.canBacktrack()) {
			src.unmark();
			return leftResult.map(a->Optional.of(a)).allowBacktrack(true);
		}
		
		src.reset(true);
		final Result rightResult = _right.parse(ctx);
		return (rightResult.isSuccessful() ? success(Optional.empty()) 
				: failure(rightResult.message())).allowBacktrack(true);
	}
}