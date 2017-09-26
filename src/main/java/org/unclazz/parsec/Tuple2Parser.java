package org.unclazz.parsec;

import java.io.IOException;

import org.unclazz.parsec.data.Tuple;
import org.unclazz.parsec.data.Tuple2;

final class Tuple2Parser<T1, T2> extends ValParser<Tuple2<T1, T2>> {
	private final ValParser<T1> _left;
	private final ValParser<T2> _right;
	
	public Tuple2Parser(ValParser<T1> left, ValParser<T2> right) {
		ParsecUtility.mustNotBeNull("left", left);
		ParsecUtility.mustNotBeNull("right", right);
		_left = left;
		_right = right;
	}

	@Override
	protected ValResultCore<Tuple2<T1, T2>> doParse(Context ctx) throws IOException {
		final ValResult<T1> leftResult = _left.parse(ctx);
		if (!leftResult.isSuccessful()) return failure(leftResult.message());
		
		final ValResult<T2> rightResult = _right.parse(ctx);
		final boolean canBacktrack = leftResult.canBacktrack() && rightResult.canBacktrack();
		
		if (rightResult.isSuccessful()) {
			return success(Tuple.of(leftResult.value(), rightResult.value()))
					.allowBacktrack(canBacktrack);
		}
		return failure(rightResult.message()).allowBacktrack(canBacktrack);
	}
}