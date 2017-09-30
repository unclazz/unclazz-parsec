package org.unclazz.parsec.sample.json;

import java.util.HashMap;
import java.util.Map;

public final class JsonObjectBuilder {
	JsonObjectBuilder() {}
	
	private final Map<String, Json> _buf = new HashMap<>();
	
	public JsonObjectBuilder append(String propName, Json propValue) {
		if (propName == null || propValue == null) throw new NullPointerException();
		_buf.put(propName, propValue);
		return this;
	}
	public JsonObjectBuilder append(JsonProperty prop) {
		if (prop == null) throw new NullPointerException();
		_buf.put(prop.name(), prop.value());
		return this;
	}
	
	public Json build() {
		if (_buf.isEmpty()) return Json.of(new JsonProperty[0]);
		return new JsonObject(_buf);
	}
}
