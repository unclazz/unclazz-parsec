package org.unclazz.parsec;

abstract class ResultCoreSupport{
	private final boolean _successful;
	private final String _message;
	private final boolean _canBacktrack;
	
	protected ResultCoreSupport(boolean successful, String message, boolean canBacktrack) {
		_successful = successful;
		_message = message;
		_canBacktrack = canBacktrack;
	}
	
	public boolean isSuccessful() {
		return _successful;
	}
	public String message() {
		return _message;
	}
	public boolean canBacktrack() {
		return _canBacktrack;
	}
}