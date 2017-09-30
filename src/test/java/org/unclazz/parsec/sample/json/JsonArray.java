package org.unclazz.parsec.sample.json;

import java.util.Collection;
import java.util.List;

import org.unclazz.parsec.util.ReadOnlyList;

final class JsonArray extends Json {
	private final ReadOnlyList<Json> _values;
	private String _toStringCache;
	JsonArray(Json... values){
		_values = ReadOnlyList.of(values);
	}
	JsonArray(Collection<? extends Json> values){
		_values = ReadOnlyList.of(values);
	}
	@Override
	public boolean isArray() {
		return true;
	}
	@Override
	public List<Json> arrayElements() {
		return _values;
	}
	@Override
	public String toString() {
		if (_toStringCache == null) {
			final StringBuilder buf = new StringBuilder().append('[');
			for (int i = 0; i < _values.size(); i ++) {
				if (i > 0) buf.append(',');
				buf.append(_values.get(i));
			}
			_toStringCache = buf.append(']').toString();
		}
		return _toStringCache;
	}
}