package org.unclazz.parsec.sample.json;

final class JsonNumber extends Json {
	private final double _value;
	JsonNumber(double value) {
		_value = value;
	}
	@Override
	public boolean isNumber() {
		return true;
	}
	@Override
	public double numberValue() {
		return _value;
	}
	@Override
	public String toString() {
		return Double.toString(_value);
	}
}