package org.unclazz.parsec;

import java.util.function.Supplier;
import java.util.regex.Pattern;

abstract class ParserSupport{
	private static final Pattern _classSuffix = Pattern.compile("Parser$");
	private final String _name;
	
	protected ParserSupport() {
		this(null);
	}
	protected ParserSupport(final String name) {
		if (name == null) {
			_name = _classSuffix.matcher(this.getClass().getName()).replaceAll("");
		}else {
			_name = name;
		}
	}
	
	protected String name() {
		return _name;
	}
	protected Parser exact(char ch){
		return new ExactCharParser(ch);
	}
	protected Parser charIn(String chs){
		return new CharClassParser(CharClass.anyOf(chs.toCharArray()));
	}
	protected Parser charIn(char...chs){
		return new CharClassParser(CharClass.anyOf(chs));
	}
	protected Parser charIn(CharClass clazz){
		return new CharClassParser(clazz);
	}
	protected Parser charBetween(char start, char end){
		return new CharClassParser(CharClass.between(start, end));
	}
	protected Parser keyword(String keyword) {
		return new KeywordParser(keyword);
	}
	protected<T> ValParser<T> product(T value) {
		return product(() -> value);
	}
	protected<T> ValParser<T> product(Supplier<T> func) {
		return new ProductParser<>(func);
	}
	protected Parser lazy(Supplier<Parser> func) {
		return new LazyParser(func);
	}
	protected<T> ValParser<T> lazyVal(Supplier<ValParser<T>> func) {
		return new LazyValParser<>(func);
	}
}