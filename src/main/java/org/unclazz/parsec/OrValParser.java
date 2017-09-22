package org.unclazz.parsec;

import java.io.IOException;

final class OrValParser<T> extends ValParser<T>{
	private final ValParser<T> _left;
	private final ValParser<T> _right;
	
	protected OrValParser(ValParser<T> left, ValParser<T> right) {
		_left = left;
		_right = right;
	}
	
	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		
		final ValResult<T> leftResult = _left.parse(ctx);
		if (leftResult.isSuccessful() || !leftResult.canBacktrack()) {
			src.unmark();
			return leftResult.allowBacktrack(true);
		}
		
		src.reset(true);
		final ValResult<T> rightResult = _right.parse(ctx);
		return rightResult.allowBacktrack(true);
	}
}