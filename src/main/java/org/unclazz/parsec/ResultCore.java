package org.unclazz.parsec;

import java.util.function.Consumer;

public class ResultCore extends ResultCoreSupport {
	private static final ResultCore _successfulSingleton = new ResultCore(true, null, true);
	public static final ResultCore ofSuccess() {
		return _successfulSingleton;
	}
	public static final ResultCore ofFailure(String message) {
		ParsecUtility.mustNotBeNull("message", message);
		return new ResultCore(false, message, true);
	}
	
	ResultCore(boolean successful, String message, boolean canBacktrack){
		super(successful, message, canBacktrack);
	}
	
	public Result attachPosition(CharPosition start, CharPosition end) {
		return new Result(isSuccessful(), message(), canBacktrack(), start, end);
	}
	public ResultCore allowBacktrack(boolean yesNo){
		return new ResultCore(isSuccessful(), message(), yesNo);
	}
	public void ifSuccessful(Runnable action) {
		if (isSuccessful()) action.run();
	}
	public void ifSuccessful(Runnable action, Consumer<String> orElse) {
		if (isSuccessful()) action.run();
		else orElse.accept(message());
	}
	public void ifFailed(Consumer<String> action) {
		if (!isSuccessful()) action.accept(message());
	}
	public ResultCore or(ResultCore other){
		if (isSuccessful()) return this;
		else return other;
	}
}
