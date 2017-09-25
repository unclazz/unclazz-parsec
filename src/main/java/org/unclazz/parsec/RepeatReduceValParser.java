package org.unclazz.parsec;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * パターンの繰返しを読み取り集約を行うパーサーです。
 * <p>このパーサーは元になるパーサーを用いたパースを指定回数繰返しつつ、そのパーサーが返した値を集約していきます。
 * 繰り返しの最小最大やセパレータの有無、どのような集約オペレーションを行うかはコンスタラクタ引数を通じて指定することができます。</p>
 * 
 * @param <T> 繰り返されるパターンの型
 * @param <U> 集約のシードと集約過程の値の型
 * @param <V> 集約結果の値の型
 */
class RepeatReduceValParser<T,U,V> extends ValParser<V> {
	private final ValParser<T> _original;
	private final RepeatConfig _repConf;
	private final ReduceConfig<T,U,V> _redConf;

	RepeatReduceValParser(ValParser<T> original, RepeatConfig repConf, ReduceConfig<T,U,V> redConf) {
			ParsecUtility.mustNotBeNull("original", original);
		_original = original;
		_repConf = repConf;
		_redConf = redConf;
	}

	@Override
	protected ValResultCore<V> doParse(Context ctx) throws IOException {
		final TextReader src = ctx.source();
		U acc = _redConf.seedFactory.get();
		
        // 予め指定された回数のパースを試みる
		for (int i = 1; i <= _repConf.maximum; i++) {
            // min ＜ ループ回数 ならリセットのための準備
            if (_repConf.breakable && _repConf.minimal < i) src.mark();
			
            // ループが2回目 かつ セパレーターのパーサーが指定されている場合
            if (1 < i && _repConf.separator != null) {
                // セパレーターのトークンのパース
                final Result sepResult = _repConf.separator.parse(ctx);
                if (!sepResult.isSuccessful()) {
                    if (_repConf.breakable && _repConf.minimal < i) {
                        // min ＜ ループ回数 なら失敗とせずリセットしてループを抜ける
                        src.reset(true);
                        break;
                    }
                    return failure(sepResult.message());
                }
            }

            final ValResult<T> mainResult = _original.parse(ctx);
            if (!mainResult.isSuccessful()) {
                if (_repConf.breakable && _repConf.minimal < i) {
                    // min ＜ ループ回数 なら失敗とせずリセットしてループを抜ける
                    src.reset(true);
                    break;
                }
                return failure(mainResult.message());
            }

            // ループ回数のシードの有無を確認
            acc = _redConf.accumulator.apply(acc, mainResult.value());

            // min ＜ ループ回数 ならリセットのための準備を解除
            if (_repConf.breakable && _repConf.minimal < i) src.unmark();
		}
		
		return success(_redConf.resultSelector.apply(acc));
	}
	RepeatReduceValParser<T,Optional<T>,Optional<T>> reReduce(BiFunction<T, T, T> accumulator){
		final BiFunction<Optional<T>, T, Optional<T>> accumulator2 =
				(a, b) -> a.isPresent() ? Optional.of(accumulator.apply(a.get(), b)) : a;
		return new RepeatReduceValParser<>(_original, 
				_repConf, new ReduceConfig<>(Optional::empty, accumulator2, a -> a));
	}
	<U2> RepeatReduceValParser<T,U2,U2> reReduce(Supplier<U2> seedFactory,BiFunction<U2, T, U2> accumulator){
		return new RepeatReduceValParser<>(_original, 
				_repConf, new ReduceConfig<>(seedFactory, accumulator, a -> a));
	}
	<U2, V2> RepeatReduceValParser<T,U2,V2> reReduce(Supplier<U2> seedFactory,
			BiFunction<U2, T, U2> accumulator, Function<U2, V2> resultSelector){
		return new RepeatReduceValParser<>(_original, 
				_repConf, new ReduceConfig<>(seedFactory, accumulator, resultSelector));
	}
}
final class RepeatConfig{
	public static RepeatConfig exactly(int v, Parser sep) {
		return new RepeatConfig(0, -1, v, sep);
	}
	public static RepeatConfig minimal(int v, Parser sep) {
		return new RepeatConfig(v, -1, -1, sep);
	}
	public static RepeatConfig maximum(int v, Parser sep) {
		return new RepeatConfig(0, v, -1, sep);
	}
	public static RepeatConfig range(int min, int max, Parser sep) {
		return new RepeatConfig(min, max, -1, sep);
	}
	
	public final int minimal;
	public final int maximum;
	public final boolean breakable;
	public final Parser separator;
	
	private RepeatConfig(int min, int max, int exactly, Parser sep){
		if (exactly == -1) {
			max = max == -1 ? Integer.MAX_VALUE : max;
			min = min == -1 ? 0 : min;
			
            if (max < 1) throw new IllegalArgumentException();
            if (min < 0) throw new IllegalArgumentException();
            if (max < min) throw new IllegalArgumentException();
            
            minimal = min;
            maximum = max;
            breakable = min != max;
		}else {
			if (exactly <= 1)  throw new IllegalArgumentException();
			minimal = exactly;
			maximum = exactly;
			breakable = false;
		}
		separator = sep;
	}
}
final class ReduceConfig<T,U,V>{
	public final Supplier<U> seedFactory;
	public final BiFunction<U, T, U> accumulator;
	public final Function<U, V> resultSelector;
	ReduceConfig(Supplier<U> seedFactory, BiFunction<U, T, U> accumulator, Function<U, V> resultSelector) {
		ParsecUtility.mustNotBeNull("seedFactory", seedFactory);
		ParsecUtility.mustNotBeNull("accumulator", accumulator);
		ParsecUtility.mustNotBeNull("resultSelector", resultSelector);
		
		this.seedFactory = seedFactory;
		this.accumulator = accumulator;
		this.resultSelector = resultSelector;
	}
}