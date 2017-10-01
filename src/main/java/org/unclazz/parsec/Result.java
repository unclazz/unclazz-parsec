package org.unclazz.parsec;

/**
 * {@link Parser}によるパースの結果を表すオブジェクトです。
 * <p>{@link ResultCore}の下位型であり、パース前後の文字位置情報も持ちます。</p>
 */
public final class Result extends ResultCore {
	private final CharPosition _start;
	private final CharPosition _end;

	Result(boolean successful, String message, boolean canBacktrack, CharPosition start, CharPosition end){
		super(successful, message, canBacktrack);
		_start = start;
		_end = end;
	}
	
	/**
	 * パース開始時の文字位置（パースしたシーケンスの最初の文字の文字位置）です。
	 * @return 文字位置
	 */
	public CharPosition start() {
		return _start;
	}
	/**
	 * パース終了時の文字位置（パースしたシーケンスの最後の文字の次の文字位置）です。
	 * @return 文字位置
	 */
	public CharPosition end() {
		return _end;
	}
	@Override
	public Result allowBacktrack(boolean yesNo){
		return new Result(isSuccessful(), message(), yesNo, _start, _end);
	}
	/**
	 * 値を紐付け{@link ValParser}のパース結果を表すオブジェクトに変換します。
	 * @param value 任意の値
	 * @return 値を持つパース結果オブジェクト
	 */
	public<T> ValResult<T> attachValue(T value) {
		return new ValResult<>(isSuccessful(), value, message(), canBacktrack(), _start, _end);
	}
	/**
	 * 型情報だけを紐付け{@link ValParser}のパース結果を表すオブジェクトに変換します。
	 * @return 値を持つパース結果オブジェクト
	 */
	public<T> ValResult<T> attachValue() {
		return new ValResult<>(isSuccessful(), null, message(), canBacktrack(), _start, _end);
	}
}
