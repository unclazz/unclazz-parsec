package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public abstract class ValParser<T> extends ParserSupport {
	protected ValParser() { }
	protected ValParser(String name) {
		super(name);
	}
	
	protected abstract ValResultCore<T> doParse(Context ctx) throws IOException;
	
	public ValResult<T> parse(String text) {
		try {
			return parse(TextReader.from(text));
		} catch (final IOException ex) {
			throw new RuntimeException("unexpected error has occurred.", ex);
		}
	}
	public ValResult<T> parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	public ValResult<T> parse(Context ctx) throws IOException{
		final TextReader src = ctx.source();
		final CharPosition startedOn = src.position();
		ctx.preParse(name());
		final ValResultCore<T> core = doParse(ctx);
		ctx.postParse(core);
		if (core instanceof ValResult) return (ValResult<T>) core;
		final CharPosition endedOn = src.position();
		return core.attachPosition(startedOn, endedOn);
	}
	protected ValResultCore<T> success(T capture){
		return ValResultCore.ofSuccess(capture);
	}
	protected ValResultCore<T> failure(String message){
		return ValResultCore.ofFailure(message);
	}
	protected ValResultCore<T> failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	public<U> ValParser<U> map(Function<T, U> func){
		return new MapParser<>(this, func);
	}
	public ValParser<Optional<T>> orNot() {
		return new OptValParser<T>(this);
	}
	public ValParser<T> then(Parser other){
		return new ThenTakeLeftParser<>(this, other);
	}
}
