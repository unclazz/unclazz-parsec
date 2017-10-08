package org.unclazz.parsec.sample.csv;

public final class Cell {
	private static final Cell _emptyNoQuote = new Cell("", false);
	private static final Cell _emptyQuoted = new Cell("", true);
	public static Cell of(String value) {
		if (value == null) throw new NullPointerException("\"value\" must not be null.");
		if (value.length() == 0) return _emptyNoQuote;
		return new Cell(value, false);
	}
	public static Cell of(String value, boolean quoted) {
		if (value == null) throw new NullPointerException();
		if (value.length() == 0) return quoted ? _emptyQuoted : _emptyNoQuote;
		return new Cell(value, quoted);
	}
	
	private final boolean _quoted;
	private final String _value;
	private Cell(String value, boolean quoted) {
		_value = value;
		_quoted = quoted;
	}
	public String value() {
		return _value;
	}
	public boolean isQuoted() {
		return _quoted;
	}
	@Override
	public String toString() {
		if (_quoted) {
			final StringBuilder buf = new StringBuilder().append('"');
			for (int i = 0; i < _value.length(); i ++) {
				final char ch = _value.charAt(i);
				if (ch == '"') buf.append('"');
				buf.append(ch);
			}
			return buf.append('"').toString();
		} else {
			return _value;
		}
	}
}
