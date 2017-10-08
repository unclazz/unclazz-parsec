package org.unclazz.parsec;

import java.io.IOException;

final class CharClassParser extends Parser {
	private final CharClass _clazz;
	CharClassParser(CharClass clazz){
		super("CharClass");
		ParsecUtility.mustNotBeNull("clazz", clazz);
		_clazz = clazz;
		param("class", clazz);
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		final int ch = src.peek();
		if (_clazz.contains(ch)) {
			src.read();
			return success();
		}
		return failure("a member of class (%s) expected but %s found.",
						_clazz, ParsecUtility.charToString(ch));
	}
}