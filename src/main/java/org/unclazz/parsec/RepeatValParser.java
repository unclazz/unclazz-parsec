package org.unclazz.parsec;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * パターンの繰返しをパースするパーサーです。
 * <p>このパーサーのインスタンスは{@link ValParser#rep()}およびその多重定義から得られます。</p>
 * @param <T> 
 */
public final class RepeatValParser<T> extends ValParser<List<T>> {
	private final ListParser<T> _inner;
	
	RepeatValParser(ValParser<T> original, int min, int max, int exactly, Parser sep) {
		ParsecUtility.mustNotBeNull("original", original);
		final RepeatConfig repConf = exactly == -1 
				? RepeatConfig.range(min, max, sep)
				: RepeatConfig.exactly(exactly, sep);
		_inner = new ListParser<T>(original, repConf);
	}

	@Override
	protected ValResultCore<List<T>> doParse(Context ctx) throws IOException {
		return _inner.parse(ctx);
	}
	
	/**
	 * リダクションを行うパーサーを返します。
	 * @param accumulator 
	 * @return
	 */
	public ValParser<Optional<T>> reduce(BiFunction<T, T, T> accumulator){
		return _inner.reReduce(accumulator);
	}
	/**
	 * リダクションを行うパーサーを返します。
	 * @param seed
	 * @param accumulator
	 * @return
	 */
	public<U> ValParser<U> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator){
		return _inner.reReduce(seed, accumulator);
	}
	/**
	 * リダクションを行うパーサーを返します。
	 * @param seed
	 * @param accumulator
	 * @param resultSelector
	 * @return
	 */
	public<U,R> ValParser<R> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator, Function<U, R> resultSelector){
		return _inner.reReduce(seed, accumulator, resultSelector);
	}
	/**
	 * 繰返しの回数をカウントするパーサーを返します。
	 * @return
	 */
	public ValParser<Integer> count() {
		return _inner.reReduce(() -> 0, (a, b) -> a + 1);
	}
}