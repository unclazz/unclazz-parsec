package org.unclazz.parsec;

import java.io.IOException;

final class OrParser extends Parser{
	private final Parser _left;
	private final Parser _right;
	
	protected OrParser(Parser left, Parser right) {
		_left = left;
		_right = right;
	}
	
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		src.mark();
		
		final Result leftResult = _left.parse(ctx);
		if (leftResult.isSuccessful() || !leftResult.canBacktrack()) {
			src.unmark();
			return leftResult.allowBacktrack(true);
		}
		
		src.reset(true);
		final Result rightResult = _right.parse(ctx);
		return rightResult.allowBacktrack(true);
	}
}