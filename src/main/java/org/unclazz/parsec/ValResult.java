package org.unclazz.parsec;

import java.util.function.Function;

/**
 * {@link ValParser}のパースの結果を表すオブジェクトです。
 * <p>{@link ValResultCore}の下位型であり、パース前後の文字位置情報も持ちます。</p>
 * @param <T> キャプチャ値の型
 */
public final class ValResult<T> extends ValResultCore<T> {
	private final CharPosition _start;
	private final CharPosition _end;

	ValResult(boolean successful, T value, String message, boolean canBacktrack, CharPosition start, CharPosition end){
		super(successful, value, message, canBacktrack);
		_start = start;
		_end = end;
	}
	
	/**
	 * パース開始時の文字位置です。
	 * @return
	 */
	public CharPosition start() {
		return _start;
	}
	/**
	 * パース終了時の文字位置です。
	 * @return
	 */
	public CharPosition end() {
		return _end;
	}
	@Override
	public<R> ValResult<R> map(Function<T, R> func) {
		if (isSuccessful()) return new ValResult<R>(isSuccessful(), func.apply(value()), null, canBacktrack(), _start, _end);
		else  return new ValResult<R>(isSuccessful(), null, message(), canBacktrack(), _start, _end);
	}
	@Override
	public ValResult<T> allowBacktrack(boolean yesNo){
		return new ValResult<>(isSuccessful(), value(), message(), yesNo, _start, _end);
	}
	/**
	 * 値との紐付けを解除して{@link Parser}のパース結果を表すオブジェクトに変換します。
	 * @return
	 */
	public Result detachValue(){
		return new Result(isSuccessful(), message(), canBacktrack(), _start, _end);
	}
}
