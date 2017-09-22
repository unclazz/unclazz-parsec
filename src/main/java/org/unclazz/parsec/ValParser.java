package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import org.unclazz.parsec.data.Seq;
import org.unclazz.parsec.data.Tuple2;

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
		return new MapValParser<>(this, func);
	}
	public ValParser<Optional<T>> opt() {
		return new OptValParser<T>(this);
	}
	public ValParser<T> then(Parser other){
		return new ThenTakeLeftParser<>(this, other);
	}
	public<U> ValParser<Tuple2<T, U>> then(ValParser<U> other){
		return new Tuple2Parser<>(this, other);
	}
	public ValParser<Seq<T>> rep(){
		return new RepeatValParser<>(this, 0, -1, -1, null);
	}
	public ValParser<Seq<T>> rep(Parser sep){
		return new RepeatValParser<>(this, 0, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @return
	 */
	public ValParser<Seq<T>> repMin(int min) {
		return new RepeatValParser<>(this, min, -1, -1, null);
	}
	/**
	 * パターンの{@code min}回以上上限なしの繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> repMin(int min, Parser sep) {
		return new RepeatValParser<>(this, min, -1, -1, sep);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @return
	 */
	public ValParser<Seq<T>> rep(int min, int max) {
		return new RepeatValParser<>(this, min, max, -1, null);
	}
	/**
	 * パターンの{@code min}回以上{@code max}回以下の繰返しにマッチするパーサーを返します。
	 * @param min
	 * @param max
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> rep(int min, int max, Parser sep) {
		return new RepeatValParser<>(this, min, max, -1, sep);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @return
	 */
	public ValParser<Seq<T>> rep(int exactly) {
		return new RepeatValParser<>(this, -1, -1, exactly, null);
	}
	/**
	 * パターンの{@code exactly}回の繰返しにマッチするパーサーを返します。
	 * @param exactly
	 * @param sep
	 * @return
	 */
	public ValParser<Seq<T>> rep(int exactly, Parser sep) {
		return new RepeatValParser<>(this, -1, -1, exactly, sep);
	}
}
