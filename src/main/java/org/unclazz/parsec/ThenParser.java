package org.unclazz.parsec;

import java.io.IOException;

final class ThenParser extends Parser {
	private final Parser _left;
	private final Parser _right;
	
	protected ThenParser(Parser left, Parser right) {
		ParsecUtility.mustNotBeNull("left", left);
		ParsecUtility.mustNotBeNull("right", right);
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