package org.unclazz.parsec.sample.json;

import java.io.IOException;

import org.unclazz.parsec.CharClass;
import org.unclazz.parsec.Context;
import org.unclazz.parsec.Mappers;
import org.unclazz.parsec.Parser;
import org.unclazz.parsec.ValParser;
import org.unclazz.parsec.ValResultCore;

final class JsonParser extends ValParser<Json> {
	// Number
	private final Parser _signOpt = charIn("+-").opt();
	private final Parser _digits = charsWhileIn(CharClass.numeric(), 1);
	private final Parser _exponentOpt = charIn("Ee").then(_signOpt).then(_digits).opt();
	private final Parser _fractionalOpt = exact('.').then(_digits).opt();
	private final Parser _integral = exact('0').or(charBetween('1', '9').then(_digits.opt()));
	private final ValParser<Json> _number = _signOpt.then(_integral)
			.then(_fractionalOpt).then(_exponentOpt).map(Double::parseDouble).map(Json::of);

	// Null
	private final ValParser<Json> _null = keyword("null").means(Json::ofNull);
	
	// Boolean
	private final ValParser<Json> _boolean = keywordIn("true", "false")
			.map(a -> Json.of("true".equals(a)));

	// String
	private final Parser _unicodeEscape = exact('u').then(charIn(CharClass.hexDigit()).rep(4));
	private final Parser _escape = exact('\\').then(charIn("\"/\\bfnrt").or(_unicodeEscape));
	private final Parser _quote = exact('"');
	private final ValParser<Json> _string = space().then(_quote).cut()
			.then(charNotIn("\"\\").or(_escape).rep().val())
			.then(_quote).map(Mappers::jsonString).map(Json::of);
	
	// Array
	private ValParser<Json> array() {
		return exact('[').cut()
				.then(lazy(this::jsonExp).rep(exact(',')).map(Json::of))
				.then(space())
				.then(exact(']'));
	}
	
	// Array
	private ValParser<JsonProperty> pair() {
		return space().then(_string).then(space())
				.then(exact(':'))
				.then(this::jsonExp)
				.map(a -> JsonProperty.of(a.item1().stringValue(), a.item2()));
	}
	
	// Object
	private ValParser<Json> object(){
		return exact('{').cut().then(pair().rep(exact(','))
				.reduce(Json::objectBuilder, JsonObjectBuilder::append, JsonObjectBuilder::build))
				.then(space())
				.then(exact('}'));
	}
	
	// JSON
	private ValParser<Json> jsonExp(){
		return space()
				.then(object().or(lazy(this::array).or(_string.or(_boolean.or(_null.or(_number))))))
				.then(space());
	}
	private ValParser<Json> _cache;
	
	@Override
	protected ValResultCore<Json> doParse(Context ctx) throws IOException {
		if (_cache == null) _cache = jsonExp();
		return _cache.parse(ctx);
	}

}
