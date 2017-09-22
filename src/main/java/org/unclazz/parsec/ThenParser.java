package org.unclazz.parsec;

import java.io.IOException;

final class ThenParser extends Parser {
	private final Parser _left;
	private final Parser _right;
	
	protected ThenParser(Parser left, Parser right) {
		_left = left;
		_right = right;
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
        // 左側のパーサーでパース
		final Result lres = _left.parse(ctx);
        // 結果NGの場合、ただちにその結果を呼び出し元に帰す
		if (!lres.isSuccessful()) return lres;
		
        // 右側のパーサーでパース
		final Result rres = _right.parse(ctx);
        // バックトラック設定を合成
		final boolean canBacktrack = lres.canBacktrack() && rres.canBacktrack();
        // 右側の結果を、バックトラック設定のみカスタマイズし、呼び出し元に返す
		return rres.allowBacktrack(canBacktrack);
	}
}
final class ThenTakeLeftParser<T> extends ValParser<T> {
	private final ValParser<T> _left;
	private final Parser _right;
	
	protected ThenTakeLeftParser(ValParser<T> left, Parser right) {
		_left = left;
		_right = right;
	}

	@Override
	protected ValResultCore<T> doParse(Context ctx) throws IOException {
        // 左側のパーサーでパース
		final ValResult<T> lres = _left.parse(ctx);
        // 結果NGの場合、ただちにその結果を呼び出し元に帰す
		if (!lres.isSuccessful()) return lres;
		
        // 右側のパーサーでパース
		final Result rres = _right.parse(ctx);
        // バックトラック設定を合成
		final boolean canBacktrack = lres.canBacktrack() && rres.canBacktrack();

		return (rres.isSuccessful() ? success(lres.value()) : failure(rres.message()))
				.allowBacktrack(canBacktrack);
	}
}
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