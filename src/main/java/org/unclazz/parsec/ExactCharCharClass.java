package org.unclazz.parsec;

/**
 * まさにその1文字を表す文字クラスです。
 */
final class ExactCharCharClass extends CharClass{
	private char _ch;
	public ExactCharCharClass(char ch) {
		_ch = ch;
	}
	@Override
	public boolean contains(int ch) {
		return _ch == ch;
	}
	@Override
	public CharClass union(CharClass other) {
		if (other.contains(_ch)) return other;
		return super.union(other);
	}
	@Override
	public CharClass plus(char ch) {
		if (_ch == ch) return this;
		return super.plus(ch);
	}
	@Override
	public String toString() {
		return ParsecUtility.charToString(_ch);
	}
}