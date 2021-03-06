package org.unclazz.parsec;

import java.io.IOException;

/**
 * パターンの繰返しをパースするパーサーです。
 * <p>このパーサーのインスタンスは{@link Parser#rep()}およびその多重定義から得られます。</p>
 */
public final class RepeatParser extends Parser {
	private final RepeatConfig _repConf;
	private final Parser _original;
	
	RepeatParser(Parser original, int min, int max, int exactly, Parser sep) {
		ParsecUtility.mustNotBeNull("original", original);
		_original = original;
		_repConf = exactly == -1 ? RepeatConfig.range(min, max, sep) : RepeatConfig.exactly(exactly, sep);
				
		param("min", min);
		param("max", max);
		param("exactly", exactly);
		param("sep", sep);
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		// 以下、RepeatReduceValParser#doParse(Context)を元にしたロジック
		
		final TextReader src = ctx.source();
		
        // 予め指定された回数のパースを試みる
		for (int i = 1; i <= _repConf.maximum; i++) {
			if (src.noRemaining() && i <= _repConf.minimal) {
				return failure("EOF has been reached.");
			}
			
            // min ＜ ループ回数 ならリセットのための準備
			final boolean marked = _repConf.breakable && _repConf.minimal < i;
            if (marked) src.mark();
			
            // ループが2回目 かつ セパレーターのパーサーが指定されている場合
            if (1 < i && _repConf.separator != null) {
                // セパレーターのトークンのパース
                final Result sepResult = _repConf.separator.parse(ctx);
                if (!sepResult.isSuccessful()) {
                    if (marked) {
                        // min ＜ ループ回数 なら失敗とせずリセットしてループを抜ける
                        src.reset(true);
                        break;
                    }
                    return failure(sepResult.message());
                }
            }

            final Result mainResult = _original.parse(ctx);
            if (!mainResult.isSuccessful()) {
                if (marked) {
                    // min ＜ ループ回数 なら失敗とせずリセットしてループを抜ける
                    src.reset(true);
                    break;
                }
                return failure(mainResult.message());
            }

            // min ＜ ループ回数 ならリセットのための準備を解除
            if (marked) src.unmark();
		}
		
		return success();
		
		// 以上、RepeatReduceValParser#doParse(Context)を元にしたロジック
	}
}