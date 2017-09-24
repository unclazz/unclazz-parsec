package org.unclazz.parsec;

/**
 * まさにその1文字を表す文字クラスです。
 */
final class ExactCharCharClass extends CharClass{
	private char _ch;
	private String _contentCache;
	public ExactCharCharClass(char ch) {
		_ch = ch;
	}
	@Override
	public boolean contains(int ch) {
		return _ch == ch;
	}
	@Override
	public CharClass union(CharClass other) {
		// レシーバが判定対象とする文字が先方クラスに含まれている場合　先方クラスをそのまま返す
		if (other.contains(_ch)) return other;
		
		// 相手がCharRangeCharClassのインスタンスである場合
		if (other instanceof CharRangeCharClass) {
			// CharRangeCharClass.unionの方がより高度な最適化が行えるため処理を委譲
			return ((CharRangeCharClass)other).union(this);
		}
		
		// それ以外の場合　親クラスに処理を委譲
		return super.union(other);
	}
	@Override
	public CharClass plus(char ch) {
		// 同じ文字の場合　自身をそのまま返す
		if (_ch == ch) return this;
		
		// 隣接している場合　文字範囲に基づくクラスを生成して返す
		if (_ch == ch - 1 || _ch == ch + 1) return between(_ch, ch);
		
		// それ以外の場合　親クラスに処理を委譲
		return super.plus(ch);
	}
	@Override
	public String toString() {
		if (_contentCache == null) {
			_contentCache = String.format("'%s'", ParsecUtility.escapeIfControl(_ch));
		}
		return _contentCache;
	}
}