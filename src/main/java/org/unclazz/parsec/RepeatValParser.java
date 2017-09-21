package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.unclazz.parsec.data.Seq;

public final class RepeatValParser<T> extends ValParser<Seq<T>> {
	private final SeqParser<T> _inner;
	
	RepeatValParser(ValParser<T> original, int min, int max, int exactly, Parser sep) {
		super("Repeat");
		final RepeatConfig repConf = exactly == -1 
				? RepeatConfig.range(min, max, sep)
				: RepeatConfig.exactly(exactly, sep);
		_inner = new SeqParser<T>(original, repConf);
	}

	@Override
	protected ValResultCore<Seq<T>> doParse(Context ctx) throws IOException {
		return _inner.parse(ctx);
	}
	
	public ValParser<T> reduce(BiFunction<T, T, T> accumulator){
		return _inner.reReduce(accumulator);
	}
	public<U> ValParser<U> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator){
		return _inner.reReduce(seed, accumulator);
	}
	public<U,R> ValParser<R> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator, Function<U, R> resultSelector){
		return _inner.reReduce(seed, accumulator, resultSelector);
	}
}