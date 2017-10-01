package org.unclazz.parsec;

/**
 * 文字位置を表すオブジェクトです。
 */
public final class CharPosition {
	private static final CharPosition _bof = new CharPosition(1, 1, 0);
	/**
	 * BOF（データソースの先頭）を示すインスタンスを返します。
	 * @return 文字位置
	 */
	public static CharPosition ofBof() {
		return _bof;
	}
	
	private final int _line;
	private final int _column;
	private final int _index;
	
	private CharPosition(int line, int column, int index) {
		_line = line;
		_column = column;
		_index = index;
	}
	
	/**
	 * 行数（{@code 1}始まり）です。
	 * @return 行数
	 */
	public int line() {
		return _line;
	}
	/**
	 * 行の先頭からの位置（{@code 1}始まり）です。
	 * @return 列数
	 */
	public int column() {
		return _column;
	}
	/**
	 * データソースの先頭からのインデックス（{@code 0}始まり）です。
	 * @return インデックス
	 */
	public int index() {
		return _index;
	}
	/**
	 * 行の先頭からの位置を{@code +1}した新しいインスタンスを返します。
	 * @return 文字位置
	 */
	public CharPosition nextColumn() {
		return new CharPosition(_line, _column + 1, _index + 1);
	}
	/**
	 * 行数を{@code +1}した新しいインスタンスを返します。
	 * @return 文字位置
	 */
	public CharPosition nextLine() {
		return new CharPosition(_line + 1, 1, _index + 1);
	}
	@Override
	public String toString() {
		return String.format("(ln: %s, col: %s, idx: %s)", _line, _column, _index);
	}
}
