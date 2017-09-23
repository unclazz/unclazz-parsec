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
	public Result allowBacktrack(boolean yesNo){
		return new Result(isSuccessful(), message(), yesNo, _start, _end);
	}
	/**
	 * 値を紐付け{@link ValParser}のパース結果を表すオブジェクトに変換します。
	 * @param value 
	 * @return
	 */
	public<T> ValResult<T> attachValue(T value) {
		return new ValResult<>(isSuccessful(), value, message(), canBacktrack(), _start, _end);
	}
	/**
	 * 型情報だけを紐付け{@link ValParser}のパース結果を表すオブジェクトに変換します。
	 * @return
	 */
	public<T> ValResult<T> attachValue() {
		return new ValResult<>(isSuccessful(), null, message(), canBacktrack(), _start, _end);
	}
}
