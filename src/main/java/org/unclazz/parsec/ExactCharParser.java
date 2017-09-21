package org.unclazz.parsec;

import java.io.IOException;

final class ExactCharParser extends Parser {
	private final char _ch;
	ExactCharParser(char ch) {
		super("ExactChar");
		_ch = ch;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final int ch = ctx.source().read();
		return ch == _ch ? success() 
				: failure("%s expected but %s found.", 
						ParsecUtility.charToString(_ch),
						ParsecUtility.charToString(ch));
	}
}

final class CharClassParser extends Parser {
	private final CharClass _clazz;
	CharClassParser(CharClass clazz){
		super("CharClass");
		ParsecUtility.mustNotBeNull("clazz", clazz);
		_clazz = clazz;
	}
	@Override
	protected ResultCore doParse(Context ctx) throws IOException {
		final int ch = ctx.source().read();
		return _clazz.contains(ch) ? success()
				: failure("a member of %s expected but %s found.",
						_clazz, ParsecUtility.charToString(ch));
	}
}
