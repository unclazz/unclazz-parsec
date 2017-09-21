package org.unclazz.parsec;

import java.util.function.Consumer;
import java.util.function.Function;

public class ValResultCore<T> extends ResultCoreSupport {
	public static final<T> ValResultCore<T> ofSuccess(T capture) {
		return new ValResultCore<T>(true, capture, null, true);
	}
	public static final<T> ValResultCore<T> ofFailure(String message) {
		ParsecUtility.mustNotBeNull("message", message);
		return new ValResultCore<T>(false, null, message, true);
	}
	
	private final T _value;
	
	ValResultCore(boolean successful, T value, String message, boolean canBacktrack){
		super(successful, message, canBacktrack);
		_value = value;
	}
	
	public T value() {
		return _value;
	}
	public<R> ValResultCore<R> map(Function<T, R> func) {
		return new ValResultCore<R>(isSuccessful(), 
				isSuccessful() ? func.apply(_value) : null, null, canBacktrack());
	}
	public ValResult<T> attachPosition(CharPosition start, CharPosition end) {
		return new ValResult<>(isSuccessful(), _value, message(), canBacktrack(), start, end);
	}
	public ValResultCore<T> allowBacktrack(boolean yesNo){
		return new ValResultCore<>(isSuccessful(), _value, message(), yesNo);
	}
	public void ifSuccessful(Consumer<T> action) {
		if (isSuccessful()) action.accept(_value);;
	}
	public void ifSuccessful(Consumer<T> action, Consumer<String> orElse) {
		if (isSuccessful()) action.accept(_value);
		else orElse.accept(message());
	}
	public void ifFailed(Consumer<String> action) {
		if (!isSuccessful()) action.accept(message());
	}
	public T orElse(T alternative) {
		return isSuccessful() ? _value : alternative;
	}
	public ValResultCore<T> or(ValResultCore<T> other){
		if (isSuccessful()) return this;
		else return other;
	}
}
