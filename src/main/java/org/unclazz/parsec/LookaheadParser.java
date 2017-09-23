package org.unclazz.parsec;

import java.io.IOException;

final class LookaheadParser extends Parser{
	private final Parser _original;
	
	LookaheadParser(Parser original) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		// _originalによるパースを試行し、結果の成否に関わらずリセットを行う
		final TextReader src = ctx.source();
		src.mark();
		final Result r = _original.parse(ctx);
		src.reset(true);
		
		// _originalのパース成否を確認しつつ結果を生成して返す
		// ※_originalのパース結果をそのまま呼び出し元に返すと、
		// パース前後の位置情報が誤ったものになってしまう。
		return r.isSuccessful() ? success() : failure(r.message());
	}
}