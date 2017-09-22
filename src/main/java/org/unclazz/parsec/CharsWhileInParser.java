package org.unclazz.parsec;

import java.io.IOException;

final class CharsWhileInParser extends Parser {
	private final CharClass _clazz;
	private final int _min;
	CharsWhileInParser(CharClass clazz, int min){
		_clazz = clazz;
		_min = min;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
        int count = 0;
        while (!src.hasReachedEof()) {
            final int ch = src.peek();
            if (!_clazz.contains(ch)) break;
            src.read();
            count++;
        }
        if (_min <= count) {
            return success();
        } else {
            final String m = String.format("expected that length of char sequence is" +
                " greater than or equal %s, but actualy it is %s.",
                _min, count);
            return failure(m);
        }
	}
}