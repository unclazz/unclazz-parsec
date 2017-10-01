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
 * @param <T> 読み取り結果型のリストの要素型
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
	 * <p>このオーバーロードはリダクションのシードとなる値を引数としてとりません。
	 * このため元のパーサーの読み取り結果が空のリストとなる場合、リダクションを行うことができません。
	 * リダクションが行えなかった場合、このメソッドが返すパーサーの読み取り結果は{@link Optional#empty()}が返す値となります。</p>
	 * @param accumulator アキュムレート（累積）のオペレーションを行う関数 
	 * @return リダクションされた結果を読み取り結果型とするパーサー
	 */
	public ValParser<Optional<T>> reduce(BiFunction<T, T, T> accumulator){
		return _inner.reReduce(accumulator);
	}
	/**
	 * リダクションを行うパーサーを返します。
	 * <p>このオーバーロードはリダクションのシードとなる値を生成する関数を引数にとります。
	 * 元のパーサーの読み取り結果が空のリストとなる場合、このシードの値がそのまま結果となります。</p>
	 * @param seed シードを生成する関数
	 * @param accumulator アキュムレート（累積）のオペレーションを行う関数 
	 * @return リダクションされた結果を読み取り結果型とするパーサー
	 * @param <U> アキュムレートの過程・結果の値の型
	 */
	public<U> ValParser<U> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator){
		return _inner.reReduce(seed, accumulator);
	}
	/**
	 * リダクションを行うパーサーを返します。
	 * <p>このオーバーロードはリダクションのシードとなる値を生成する関数を引数にとります。
	 * 元のパーサーの読み取り結果が空のリストとなる場合、このシードの値がそのままアキュムレートの結果となります。
	 * このオーバーロードはまたアキュムレート結果を変換して最終結果を得るための関数を引数に取ります。</p>
	 * @param seed シードを生成する関数
	 * @param accumulator アキュムレート（累積）のオペレーションを行う関数 
	 * @param resultSelector アキュムレートの結果を変換して最終結果を得るための関数
	 * @return リダクションされた結果を読み取り結果型とするパーサー
	 * @param <U> アキュムレートの過程・結果の値の型
	 * @param <R> アキュムレート結果を変換した最終結果の値の型
	 */
	public<U,R> ValParser<R> reduce(Supplier<U> seed, BiFunction<U, T, U> accumulator, Function<U, R> resultSelector){
		return _inner.reReduce(seed, accumulator, resultSelector);
	}
	/**
	 * 繰返しの回数をカウントするパーサーを返します。
	 * @return 繰返しの回数をカウントするパーサー
	 */
	public ValParser<Integer> count() {
		return _inner.reReduce(() -> 0, (a, b) -> a + 1);
	}
}