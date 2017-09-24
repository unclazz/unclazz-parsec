package org.unclazz.parsec;

/**
 * 補集合を表す文字クラスです。
 */
final class ComplementCharClass extends CharClass{
	private final CharClass _clazz;
	private String _contentCache;
	public ComplementCharClass(CharClass clazz) {
		ParsecUtility.mustNotBeNull("clazz", clazz);
		_clazz = clazz;
	}
	@Override
	public boolean contains(int ch) {
		return !_clazz.contains(ch);
	}
	@Override
	public String toString() {
		if (_contentCache == null) {
			_contentCache = String.format("not %s", _clazz);
		}
		return _contentCache;
	}
}