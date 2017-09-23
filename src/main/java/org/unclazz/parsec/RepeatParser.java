package org.unclazz.parsec;

import java.io.IOException;

public final class RepeatParser extends Parser {
	private final SeqParser<String> _inner;
	
	RepeatParser(Parser original, int min, int max, int exactly, Parser sep) {
		ParsecUtility.mustNotBeNull("original", original);
		final RepeatConfig repConf = exactly == -1 
				? RepeatConfig.range(min, max, sep)
				: RepeatConfig.exactly(exactly, sep);
		_inner = new SeqParser<String>(original.means(""), repConf);
	}

	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		return _inner.parse(ctx).detachValue();
	}
	public ValParser<Integer> count() {
		return _inner.reReduce(() -> 0, (a, b) -> a + 1);
	}
}