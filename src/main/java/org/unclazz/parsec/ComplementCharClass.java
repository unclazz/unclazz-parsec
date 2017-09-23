package org.unclazz.parsec;

/**
 * 補集合を表す文字クラスです。
 */
final class ComplementCharClass extends CharClass{
	private final CharClass _clazz;
	public ComplementCharClass(CharClass clazz) {
		ParsecUtility.mustNotBeNull("clazz", clazz);
		_clazz = clazz;
	}
	@Override
	public boolean contains(int ch) {
		return !_clazz.contains(ch);
	}
}