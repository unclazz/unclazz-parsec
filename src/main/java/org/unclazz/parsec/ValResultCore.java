package org.unclazz.parsec;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link ValParser}のパースの成否を表すオブジェクトです。
 * <p>{@link ValResult}の上位型であり、パース前後の文字位置情報を持ちません。</p>
 * @param <T> キャプチャ値の型
 */
public class ValResultCore<T> extends ResultCoreSupport {
	/**
	 * 成功を表すインスタンスを返します。
	 * @param capture
	 * @return
	 */
	public static final<T> ValResultCore<T> ofSuccess(T capture) {
		return new ValResultCore<T>(true, capture, null, true);
	}
	/**
	 * 失敗を表すインスタンスを返します。
	 * @param message
	 * @return
	 */
	public static final<T> ValResultCore<T> ofFailure(String message) {
		ParsecUtility.mustNotBeNull("message", message);
		return new ValResultCore<T>(false, null, message, true);
	}
	
	private final T _value;
	
	ValResultCore(boolean successful, T value, String message, boolean canBacktrack){
		super(successful, message, canBacktrack);
		_value = value;
	}
	
	/**
	 * パーサによりキャプチャされた値です。
	 * @return
	 */
	public final T value() {
		return _value;
	}
	/**
	 * キャプチャされた値に関数を適用します。
	 * @param func
	 * @return
	 */
	public<R> ValResultCore<R> map(Function<T, R> func) {
		return new ValResultCore<R>(isSuccessful(), 
				isSuccessful() ? func.apply(_value) : null, null, canBacktrack());
	}
	/**
	 * パース前後の文字位置情報を付与します。
	 * @param start
	 * @param end
	 * @return
	 */
	public final ValResult<T> attachPosition(CharPosition start, CharPosition end) {
		return new ValResult<>(isSuccessful(), _value, message(), canBacktrack(), start, end);
	}
	/**
	 * 直近の{@link ValParser#or(ValParser)}を起点とするバックトラックの可否を設定します。
	 * @param yesNo
	 * @return
	 */
	public ValResultCore<T> allowBacktrack(boolean yesNo){
		return new ValResultCore<>(isSuccessful(), _value, message(), yesNo);
	}
	/**
	 * このオブジェクトが成功を表すものである場合アクションを実行します。
	 * @param action
	 */
	public void ifSuccessful(Consumer<T> action) {
		if (isSuccessful()) action.accept(_value);;
	}
	/**
	 * このオブジェクトが成功を表すものである場合第1引数のアクションを、さもなくば第2引数のアクションを実行します。
	 * @param action
	 * @param orElse
	 */
	public void ifSuccessful(Consumer<T> action, Consumer<String> orElse) {
		if (isSuccessful()) action.accept(_value);
		else orElse.accept(message());
	}
	/**
	 * このオブジェクトが失敗を表すものである場合アクションを実行します。
	 * @param action
	 */
	public final void ifFailed(Consumer<String> action) {
		if (!isSuccessful()) action.accept(message());
	}
	/**
	 * このオブジェクトが成功を表すものである場合はそのキャプチャ値を、さもなくば引数で指定された値を返します。
	 * @param alternative
	 * @return
	 */
	public T orElse(T alternative) {
		return isSuccessful() ? _value : alternative;
	}
}
