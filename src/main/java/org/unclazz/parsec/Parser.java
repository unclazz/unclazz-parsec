package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.Function;

public abstract class Parser extends ParserSupport {
	protected Parser() {}
	protected Parser(String name) {
		super(name);
	}
	
	protected abstract ResultCore doParse(Context ctx) throws IOException;
	
	public Result parse(String text) {
		try {
			return parse(TextReader.from(text));
		} catch (final IOException ex) {
			throw new RuntimeException("unexpected error has occurred.", ex);
		}
	}
	public Result parse(TextReader reader) throws IOException{
		return parse(reader.toContext());
	}
	public Result parse(Context ctx) throws IOException{
		final TextReader src = ctx.source();
		final CharPosition startedOn = src.position();
		ctx.preParse(name());
		final ResultCore core = doParse(ctx);
		ctx.postParse(core);
		if (core instanceof Result) return (Result) core;
		final CharPosition endedOn = src.position();
		return core.attachPosition(startedOn, endedOn);
	}
	protected ResultCore success(){
		return ResultCore.ofSuccess();
	}
	protected ResultCore failure(String message){
		return ResultCore.ofFailure(message);
	}
	protected ResultCore failure(String format, Object...args){
		return failure(String.format(format, args));
	}
	
	public ValParser<String> val(){
		return new CaptureParser(this);
	}
	public<T> ValParser<T> map(Function<String, T> func){
		return val().map(func);
	}
	public Parser orNot() {
		return new OptParser(this);
	}
	public Parser then(Parser other){
		return new ThenParser(this, other);
	}
	public<T> ValParser<T> then(ValParser<T> other){
		return new ThenTakeRightParser<>(this, other);
	}
}
