package org.unclazz.parsec.sample.json;

final class JsonNull extends Json {
	@Override
	public boolean isNull() {
		return true;
	}
	@Override
	public String toString() {
		return "null";
	}
}