package org.unclazz.parsec;

import java.io.IOException;

/**
 * パターンの繰返しをパースするパーサーです。
 * <p>このパーサーのインスタンスは{@link Parser#rep()}およびその多重定義から得られます。</p>
 */
public final class RepeatParser extends Parser {
	private final ListParser<String> _inner;
	
	RepeatParser(Parser original, int min, int max, int exactly, Parser sep) {
		ParsecUtility.mustNotBeNull("original", original);
		final RepeatConfig repConf = exactly == -1 
				? RepeatConfig.range(min, max, sep)
				: RepeatConfig.exactly(exactly, sep);
		_inner = new ListParser<String>(original.means(""), repConf);
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		return _inner.parse(ctx).detachValue();
	}
	/**
	 * 繰返しの回数をカウントするパーサーを返します。
	 * @return
	 */
	public ValParser<Integer> count() {
		return _inner.reReduce(() -> 0, (a, b) -> a + 1);
	}
}