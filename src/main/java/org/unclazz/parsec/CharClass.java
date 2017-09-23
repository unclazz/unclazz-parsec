package org.unclazz.parsec;

/**
 * 文字クラスを表すオブジェクトです。
 */
public abstract class CharClass {
	/**
	 * 開始位置と終了位置で表される文字クラスを返します。
	 * @param start
	 * @param end
	 * @return
	 */
	public static CharClass between(char start, char end) {
		return new CharRangeCharClass(new CharRange[]{ new CharRange(start, end) });
	}
	/**
	 * まさにその1文字を表す文字クラスを返します。
	 * @param ch
	 * @return
	 */
	public static CharClass exact(char ch) {
		return new ExactCharCharClass(ch);
	}
	/**
	 * 文字集合からなる文字クラスを返します。
	 * @param cs
	 * @return
	 */
	public static CharClass anyOf(char...cs) {
		return new CharRangeCharClass(cs);
	}
	/**
	 * 文字集合からなる文字クラスを返します。
	 * @param cs
	 * @return
	 */
	public static CharClass anyOf(String cs) {
		return new CharRangeCharClass(cs.toCharArray());
	}
	
	/**
	 * 文字が文字クラスに属しているかどうかを判定します。
	 * @param ch
	 * @return
	 */
	public abstract boolean contains(int ch);
	
	/**
	 * 文字クラスを合成します。
	 * @param other
	 * @return
	 */
	public CharClass union(CharClass other) {
		return new UnionCharClass(this, other);
	}
	/**
	 * 文字クラスにメンバーを追加します。
	 * @param ch
	 * @return
	 */
	public CharClass plus(char ch) {
		if (contains(ch)) return this;
		return new UnionCharClass(this, exact(ch));
	}
}
