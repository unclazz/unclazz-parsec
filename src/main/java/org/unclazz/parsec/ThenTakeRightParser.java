package org.unclazz.parsec;

import java.io.IOException;

final class ThenTakeRightParser<T> extends ValParser<T> {
	private final Parser _left;
	private final ValParser<T> _right;
	
	protected ThenTakeRightParser(Parser left, ValParser<T> right) {
		_left = left;
		_right = right;
	}

	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
        // 左側のパーサーでパース
		final Result lres = _left.parse(ctx);
        // 結果NGの場合、ただちにその結果を呼び出し元に帰す
		if (!lres.isSuccessful()) return failure(lres.message());
		
        // 右側のパーサーでパース
		final ValResult<T> rres = _right.parse(ctx);
        // バックトラック設定を合成
		final boolean canBacktrack = lres.canBacktrack() && rres.canBacktrack();

		return rres.allowBacktrack(canBacktrack);
	}
}