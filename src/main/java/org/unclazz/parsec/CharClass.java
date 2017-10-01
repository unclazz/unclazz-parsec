package org.unclazz.parsec;

/**
 * 文字クラスを表すオブジェクトです。
 */
public abstract class CharClass {
	/**
	 * 開始位置と終了位置で表される文字クラスを返します。
	 * @param start 開始位置の文字
	 * @param end 終了位置の文字
	 * @return 文字クラス
	 */
	public static CharClass between(char start, char end) {
		return new CharRangeCharClass(new CharRange[]{ new CharRange(start, end) });
	}
	/**
	 * まさにその1文字を表す文字クラスを返します。
	 * @param ch 任意の1文字
	 * @return 文字クラス
	 */
	public static CharClass exact(char ch) {
		return new ExactCharCharClass(ch);
	}
	/**
	 * 文字集合からなる文字クラスを返します。
	 * @param cs 文字集合
	 * @return 文字クラス
	 */
	public static CharClass anyOf(char...cs) {
		return new CharRangeCharClass(cs);
	}
	/**
	 * 文字集合からなる文字クラスを返します。
	 * @param cs 文字集合
	 * @return 文字クラス
	 */
	public static CharClass anyOf(String cs) {
		return new CharRangeCharClass(cs.toCharArray());
	}
	/**
	 * 補集合となる文字クラスを返します。
	 * @param clazz 元の文字クラス
	 * @return 文字クラス
	 */
	public static CharClass not(CharClass clazz) {
		return new ComplementCharClass(clazz);
	}
	
	private static CharClass _newline;
	private static CharClass _alphabetic;
	private static CharClass _numeric;
	private static CharClass _alphanumeric;
	private static CharClass _hexDigit;
	private static CharClass _control;
	private static CharClass _spaceAndControl;
	
	/**
	 * 改行の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass newline() {
		if (_newline == null) {
			_newline = anyOf('\r', '\n');
		}
		return _newline;
	}
	/**
	 * {@code [A-Za-z]}の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass alphabetic() {
		if (_alphabetic == null) {
			_alphabetic = between('A', 'Z').union(between('a', 'z'));
		}
		return _alphabetic;
	}
	/**
	 * {@code [0-9]}の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass numeric() {
		if (_numeric == null) {
			_numeric = between('0', '9');
		}
		return _numeric;
	}
	/**
	 * {@code [0-9A-Za-z]}の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass alphanumeric() {
		if (_alphanumeric == null) {
			_alphanumeric = numeric().union(alphabetic());
		}
		return _alphanumeric;
	}
	/**
	 * {@code [0-9A-Fa-f]}の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass hexDigit() {
		if (_hexDigit == null) {
			 _hexDigit = numeric().union(between('A', 'F')).union(between('a', 'f'));
		}
		return _hexDigit;
	}
	/**
	 * 制御文字（コードポイント{@code 0}から{@code 31}と{@code 127}）の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass control() {
		if (_control == null) {
			_control = between((char)0, (char)31).plus((char)127);
		}
		return _control;
	}
	/**
	 * 制御文字と空白文字（コードポイント{@code 32}）の文字クラスです。
	 * @return 文字クラス
	 */
	public static CharClass spaceAndControl() {
		if (_spaceAndControl == null) {
			_spaceAndControl = control().plus((char)32);
		}
		return _spaceAndControl;
	}
	
	/**
	 * 文字が文字クラスに属しているかどうかを判定します。
	 * @param ch 任意の文字
	 * @return 文字クラス
	 */
	public abstract boolean contains(int ch);
	
	/**
	 * 文字クラスを合成します。
	 * @param other 別の文字クラス
	 * @return 文字クラス
	 */
	public CharClass union(CharClass other) {
		return new UnionCharClass(this, other);
	}
	/**
	 * 文字クラスにメンバーを追加します。
	 * @param ch 任意の1文字
	 * @return 文字クラス
	 */
	public CharClass plus(char ch) {
		if (contains(ch)) return this;
		return new UnionCharClass(this, exact(ch));
	}
}
