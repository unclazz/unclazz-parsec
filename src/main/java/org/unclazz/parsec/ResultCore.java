package org.unclazz.parsec;

import java.util.function.Consumer;

/**
 * {@link Parser}によるパースの成否を表すオブジェクトです。
 * <p>{@link Result}の上位型であり、パース前後の文字位置情報を持ちません。</p>
 */
public class ResultCore extends ResultCoreSupport {
	private static final ResultCore _successfulSingleton = new ResultCore(true, null, true);
	/**
	 * 成功を表すインスタンスを返します。
	 * @return
	 */
	public static final ResultCore ofSuccess() {
		return _successfulSingleton;
	}
	/**
	 * 失敗を表すインスタンスを返します。
	 * @param message
	 * @return
	 */
	public static final ResultCore ofFailure(String message) {
		ParsecUtility.mustNotBeNull("message", message);
		return new ResultCore(false, message, true);
	}
	
	ResultCore(boolean successful, String message, boolean canBacktrack){
		super(successful, message, canBacktrack);
	}
	
	/**
	 * パース前後の文字位置情報を付与します。
	 * @param start
	 * @param end
	 * @return
	 */
	public Result attachPosition(CharPosition start, CharPosition end) {
		return new Result(isSuccessful(), message(), canBacktrack(), start, end);
	}
	/**
	 * 直近の{@link #or(ValResultCore)}を起点とするバックトラックの可否を設定します。
	 * @param yesNo
	 * @return
	 */
	public ResultCore allowBacktrack(boolean yesNo){
		return new ResultCore(isSuccessful(), message(), yesNo);
	}
	/**
	 * このオブジェクトが成功を表すものである場合アクションを実行します。
	 * @param action
	 */
	public void ifSuccessful(Runnable action) {
		if (isSuccessful()) action.run();
	}
	/**
	 * このオブジェクトが成功を表すものである場合第1引数のアクションを、さもなくば第2引数のアクションを実行します。
	 * @param action
	 * @param orElse
	 */
	public void ifSuccessful(Runnable action, Consumer<String> orElse) {
		if (isSuccessful()) action.run();
		else orElse.accept(message());
	}
	/**
	 * このオブジェクトが失敗を表すものである場合アクションを実行します。
	 * @param action
	 */
	public void ifFailed(Consumer<String> action) {
		if (!isSuccessful()) action.accept(message());
	}
}
