package org.unclazz.parsec;

public final class Result extends ResultCore {
	private final CharPosition _start;
	private final CharPosition _end;

	Result(boolean successful, String message, boolean canBacktrack, CharPosition start, CharPosition end){
		super(successful, message, canBacktrack);
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
	public Result allowBacktrack(boolean yesNo){
		return new Result(isSuccessful(), message(), yesNo, _start, _end);
	}
	public Result or(Result other){
		if (isSuccessful()) return this;
		else return other;
	}
	public<T> ValResult<T> attachValue(T value) {
		return new ValResult<>(isSuccessful(), value, message(), canBacktrack(), _start, _end);
	}
	public<T> ValResult<T> attachValue() {
		return new ValResult<>(isSuccessful(), null, message(), canBacktrack(), _start, _end);
	}
}
