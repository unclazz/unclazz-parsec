package org.unclazz.parsec.sample.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.unclazz.parsec.TextReader;
import org.unclazz.parsec.ValResult;
import org.unclazz.parsec.util.ReadOnlyList;

public abstract class Json {
	private static final Json _nullInstance = new JsonNull();
	private static final Json _trueInstance = new JsonBoolean(true);
	private static final Json _falseInstance = new JsonBoolean(false);
	private static final Json _emptyInstance = new JsonString("");
	private static final Json _zeroInstance = new JsonNumber(0);
	private static final Json _emptyArrayInstance = new JsonArray(ReadOnlyList.of());
	private static final Json _emptyObjectInstance = new JsonObject(Collections.emptyMap());
	private static final JsonParser _parser = new JsonParser();
	
	public static Json parse(String jsonText) {
		final ValResult<Json> r =  _parser.parse(jsonText);
		if (r.isSuccessful()) {
			return r.value();
		} else {
			throw new RuntimeException(r.message());
		}
	}
	public static Json parse(Reader jsonReader) throws IOException {
		final ValResult<Json> r =  _parser.parse(TextReader.from(jsonReader));
		if (r.isSuccessful()) {
			return r.value();
		} else {
			throw new RuntimeException(r.message());
		}
	}
	public static Json parse(InputStream jsonStream, Charset charset) throws IOException {
		final ValResult<Json> r =  _parser.parse(TextReader.from(jsonStream, charset));
		if (r.isSuccessful()) {
			return r.value();
		} else {
			throw new RuntimeException(r.message());
		}
	}
	public static Json parse(File jsonFile, Charset charset) throws IOException {
		final ValResult<Json> r =  _parser.parse(TextReader.from(jsonFile, charset));
		if (r.isSuccessful()) {
			return r.value();
		} else {
			throw new RuntimeException(r.message());
		}
	}
	public static Json of(String value) {
		return value.length() == 0 ? _emptyInstance : new JsonString(value);
	}
	public static Json of(boolean value) {
		return value ? _trueInstance : _falseInstance;
	}
	public static Json of(double value) {
		return value == 0.0 ? _zeroInstance : new JsonNumber(value);
	}
	public static Json ofNull() {
		return _nullInstance;
	}
	public static Json of(Json...values) {
		return values.length == 0 ? _emptyArrayInstance : new JsonArray(values);
	}
	public static Json of(Collection<? extends Json> values) {
		return values.isEmpty() ? _emptyArrayInstance : new JsonArray(values);
	}
	public static Json of(JsonProperty...props) {
		if (props.length == 0) return _emptyObjectInstance;
		final JsonObjectBuilder b = new JsonObjectBuilder();
		for (final JsonProperty p : props) b.append(p);
		return b.build();
	}
	public static Json of(Map<String,Json> props) {
		if (props.isEmpty()) return _emptyObjectInstance;
		final JsonObjectBuilder b = new JsonObjectBuilder();
		for (final Entry<String, Json> p : props.entrySet()) b.append(p.getKey(), p.getValue());
		return b.build();
	}
	public static JsonObjectBuilder objectBuilder() {
		return new JsonObjectBuilder();
	}
	
	Json() {}
	
	public double numberValue() {
		throw new IllegalStateException("not istance of Number.");
	}
	public final double numberValue(double orElse) {
		return isNumber() ? numberValue() : orElse;
	}
	public String stringValue() {
		throw new IllegalStateException("not istance of String.");
	}
	public final String stringValue(String orElse) {
		return isString() ? stringValue() : orElse;
	}
	public boolean booleanValue() {
		throw new IllegalStateException("not istance of String.");
	}
	public final boolean booleanValue(boolean orElse) {
		return isBoolean() ? booleanValue() : orElse;
	}
	public List<Json> arrayElements() {
		throw new IllegalStateException("not istance of Array.");
	}
	public List<Json> arrayElements(List<Json> orElse) {
		return isArray() ? arrayElements() : orElse;
	}
	public final Json arrayElement(int i) {
		return arrayElements().get(i);
	}
	public final Json arrayElement(int i, Json orElse) {
		return isArray() ? arrayElements().get(i) : orElse;
	}
	public Map<String, Json> objectProperties() {
		throw new IllegalStateException("not istance of Object.");
	}
	public final Map<String, Json> objectProperties(Map<String, Json> orElse) {
		return isObject() ? objectProperties() : orElse;
	}
	public final Json objectPropery(String name) {
		return objectProperties().get(name);
	}
	public final Json objectPropery(String name, Json orElse) {
		return isObject() ? objectProperties().get(name) : orElse;
	}
	public boolean isNumber() {
		return false;
	}
	public boolean isString() {
		return false;
	}
	public boolean isBoolean() {
		return false;
	}
	public boolean isNull() {
		return false;
	}
	public boolean isArray() {
		return false;
	}
	public boolean isObject() {
		return false;
	}
	public final Json ifNull(String value) {
		return isNull() ? of(value) : this;
	}
	public final Json ifNull(double value) {
		return isNull() ? of(value) : this;
	}
	public final Json ifNull(boolean value) {
		return isNull() ? of(value) : this;
	}
}
