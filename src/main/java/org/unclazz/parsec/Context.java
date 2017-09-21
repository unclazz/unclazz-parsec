package org.unclazz.parsec;

import java.io.IOException;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * パース処理のコンテキストを表すオブジェクトです。
 * {@link ValParser#parse(Context)}や{@link ValParser#doParse(Context)}の引数として使用します。
 * インスタンスは{@link TextReader}オブジェクトから得られます。
 */
public final class Context {
	private final TextReader _source;
	private final Stack<String> _stack;
	private final boolean _logging;
	private final boolean _autoSkip;
	private final Consumer<String> _logAppender;
	private final CharClass _skipTarget;
	
	Context(TextReader source){
		this(source, null, null, null);
	}
	Context(TextReader source, Stack<String> stack, Consumer<String> logAppender, CharClass skipTarget){
		_source = source;
		_stack = logAppender == null ? null : (stack == null ? new Stack<>() : stack);
		_logging = logAppender != null;
		_autoSkip = skipTarget != null;
		_logAppender = logAppender;
		_skipTarget = skipTarget;
	}
	
	Stack<String> stack(){
		return _stack;
	}
	Consumer<String> logAppender() {
		return _logAppender;
	}
	CharClass skipTarget() {
		return _skipTarget;
	}
	public TextReader source() {
		return _source;
	}
	public boolean autoSkip() {
		return _autoSkip;
	}
	public boolean logging() {
		return _logging;
	}
	public Context configure(Consumer<ContextConfigurer> action) {
		final ContextConfigurer config = new ContextConfigurer(this);
		action.accept(config);
		return config.makeContext();
	}
	public void log(String message) {
		if (_logging) _logAppender.accept(message);
	}
	public void log(String format, Object... args) {
		if (_logging) log(makeLabel(' ').append(String.format(format, args)).toString());
	}
	void preParse(String parserName) throws IOException {
		preParse_doSkip();
		if (!_logging) return;
		_stack.push(parserName);
		_logAppender.accept(makeLabel('+').toString());
	}
	private void preParse_doSkip() throws IOException {
		if (!_autoSkip) return;
		while (!_source.hasReachedEof()) {
			final char ch = (char)_source.peek();
			if (!_skipTarget.contains(ch)) break;
			_source.read();
		}
	}
	void postParse(ResultCore result) {
		if (!_logging) return;
        final StringBuilder buff = makeLabel('-');
        if (result.isSuccessful()) buff.append("Success(");
        else buff.append("Failure(").append(result.message());
        if (!result.canBacktrack()) buff.append(", cut");
        _logAppender.accept(buff.append(')').toString());
		_stack.pop();
	}
	<T> void postParse(ValResultCore<T> result) {
		if (!_logging) return;
        final StringBuilder buff = makeLabel('-');
        if (result.isSuccessful()) buff.append("Success(").append(result.value());
        else buff.append("Failure(").append(result.message());
        if (!result.canBacktrack()) buff.append(", cut");
        _logAppender.accept(buff.append(')').toString());
		_stack.pop();
	}
	private StringBuilder makeLabel(char sign) {
		final CharPosition pos = _source.position();
        final StringBuilder buff = new StringBuilder();
        final int depth = (_stack.size() - 1) * 2;
        for (int i = 0; i < depth; i++) {
        	buff.append(' ');
        }
        return buff.append(sign).append(' ').append(_stack.peek())
                .append(' ').append(pos).append(' ');
	}
}

