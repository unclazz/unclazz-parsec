package org.unclazz.parsec.sample.json;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

final class JsonObject extends Json {
	
	private final Map<String,Json> _properties;
	private String _toStringCache;
	JsonObject(Map<String,Json> properties){
		_properties = Collections.unmodifiableMap(properties);
	}
	@Override
	public boolean isObject() {
		return true;
	}
	@Override
	public Map<String,Json> objectProperties() {
		return _properties;
	}
	@Override
	public String toString() {
		if (_toStringCache == null) {
			final StringBuilder buf = new StringBuilder().append('{');
			for (final Entry<String,Json> entry : _properties.entrySet()) {
				if (buf.length() > 1) buf.append(',');
				buf.append(JsonString.quotedValue(entry.getKey()))
					.append(':').append(entry.getValue());
			}
			_toStringCache = buf.append('}').toString();
		}
		return _toStringCache;
	}
}