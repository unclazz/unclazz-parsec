package org.unclazz.parsec;

import java.util.Stack;
import java.util.function.Consumer;

public class ContextConfigurer {
	private final TextReader _reader;
	private final Stack<String> _stack;
	private Consumer<String> _logAppender;
	private CharClass _skipTarget;
	
	ContextConfigurer(Context ctx) {
		_reader = ctx.source();
		_stack = ctx.stack();
		_logAppender = ctx.logAppender();
		_skipTarget = ctx.skipTarget();
	}
	
	public ContextConfigurer setLogAppender(Consumer<String> logAppender) {
		_logAppender = logAppender;
		return this;
	}
	public ContextConfigurer setSkipTarget(CharClass skipTarget) {
		_skipTarget = skipTarget;
		return this;
	}
	Context makeContext() {
		return new Context(_reader, _stack, _logAppender, _skipTarget);
	}
}
