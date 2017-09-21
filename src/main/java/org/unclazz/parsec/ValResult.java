package org.unclazz.parsec;

import java.util.function.Function;

public final class ValResult<T> extends ValResultCore<T> {
	private final CharPosition _start;
	private final CharPosition _end;

	ValResult(boolean successful, T value, String message, boolean canBacktrack, CharPosition start, CharPosition end){
		super(successful, value, message, canBacktrack);
		_start = start;
		_end = end;
	}
	
	public CharPosition start() {
		return _start;
	}
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
	public ValResult<T> or(ValResult<T> other){
		if (isSuccessful()) return this;
		else return other;
	}
	public Result detachValue(){
		return new Result(isSuccessful(), message(), canBacktrack(), _start, _end);
	}
}
