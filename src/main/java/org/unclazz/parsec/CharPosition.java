package org.unclazz.parsec;

public final class CharPosition {
	private static final CharPosition _bof = new CharPosition(1, 1, 0);
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
	
	public int line() {
		return _line;
	}
	public int column() {
		return _column;
	}
	public int index() {
		return _index;
	}
	public CharPosition nextColumn() {
		return new CharPosition(_line, _column + 1, _index + 1);
	}
	public CharPosition nextLine() {
		return new CharPosition(_line + 1, 1, _index + 1);
	}
	@Override
	public String toString() {
		return String.format("(ln: %s, col: %s, idx: %s)", _line, _column, _index);
	}
}
