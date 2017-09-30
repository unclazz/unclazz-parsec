package org.unclazz.parsec.sample.json;

final class JsonString extends Json {
	private final String _value;
	private String _toStringCache;
	JsonString(String value) {
		if (value == null) throw new NullPointerException();
		_value = value;
	}
	@Override
	public boolean isString() {
		return true;
	}
	@Override
	public String stringValue() {
		return _value;
	}
	@Override
	public String toString() {
		if (_toStringCache == null) {
			_toStringCache = quotedValue(_value);
		}
		return _toStringCache;
	}
	static String quotedValue(String rawValue) {
		final StringBuilder buf = new StringBuilder().append('"');
		for (int i = 0; i < rawValue.length(); i ++) {
			final char ch = rawValue.charAt(i);
			switch(ch) {
			case '"': buf.append("\\\""); break;
			case '\\': buf.append("\\\\"); break;
			case '/': buf.append("\\/"); break;
			case '\b': buf.append("\\\b"); break;
			case '\f': buf.append("\\\f"); break;
			case '\n': buf.append("\\\n"); break;
			case '\r': buf.append("\\\r"); break;
			case '\t': buf.append("\\\t"); break;
			default: buf.append(ch);
			}
		}
		return buf.append('"').toString();
	}
}