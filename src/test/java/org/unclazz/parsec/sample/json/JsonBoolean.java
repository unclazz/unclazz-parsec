package org.unclazz.parsec.sample.json;

final class JsonBoolean extends Json {
	private final boolean _value;
	JsonBoolean(boolean value) {
		_value = value;
	}
	@Override
	public boolean isBoolean() {
		return true;
	}
	@Override
	public boolean booleanValue() {
		return _value;
	}
	@Override
	public String toString() {
		return _value ? "true" : "false";
	}
}