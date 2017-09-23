package org.unclazz.parsec;

import java.util.Stack;
import java.util.function.Consumer;

/**
 * {@link Context}の構成変更を行うためのクラスです。
 */
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
	
	/**
	 * ログ・アペンダーを設定します。
	 * {@code null}を設定するとログ機能は無効化されます。
	 * @param logAppender アペンダーとして機能するコンシューマー関数
	 * @return
	 */
	public ContextConfigurer setLogAppender(Consumer<String> logAppender) {
		_logAppender = logAppender;
		return this;
	}
	/**
	 * 自動スキップの対象を設定します。
	 * {@code null}を設定すると自動スキップ機能は無効化されます。
	 * @param skipTarget スキップ対象を示す文字クラス
	 * @return
	 */
	public ContextConfigurer setSkipTarget(CharClass skipTarget) {
		_skipTarget = skipTarget;
		return this;
	}
	Context makeContext() {
		return new Context(_reader, _stack, _logAppender, _skipTarget);
	}
}
