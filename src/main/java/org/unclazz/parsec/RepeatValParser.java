package org.unclazz.parsec;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.unclazz.parsec.data.ReadOnlyList;

public final class RepeatValParser<T> extends ValParser<ReadOnlyList<T>> {
	private final ListParser<T> _inner;
	
	RepeatValParser(ValParser<T> original, int min, int max, int exactly, Parser sep) {
		ParsecUtility.mustNotBeNull("original", original);
		final RepeatConfig repConf = exactly == -1 
				? RepeatConfig.range(min, max, sep)
				: RepeatConfig.exactly(exactly, sep);
		_inner = new ListParser<T>(original, repConf);
	}

	@Override
	protected ValResultCore<ReadOnlyList<T>> doParse(Context ctx) throws IOException {
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
	public ValParser<Integer> count() {
		return _inner.reReduce(() -> 0, (a, b) -> a + 1);
	}
}