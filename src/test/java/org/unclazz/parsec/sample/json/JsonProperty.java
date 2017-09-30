package org.unclazz.parsec.sample.json;

public final class JsonProperty {
	public static JsonProperty of(String name, Json value) {
		return new JsonProperty(name, value);
	}
	
	private final String _name;
	private final Json _value;
	
	private JsonProperty(String name, Json value) {
		if (name == null || value == null) throw new NullPointerException();
		_name = name;
		_value = value;
	}
	
	public String name() {
		return _name;
	}
	public Json value() {
		return _value;
	}
	@Override
	public String toString() {
		return String.format("JsonProperty(name=%s, value=%s)", _name, _value);
	}
}
