package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * {@link Reader}に{@link #peek()}機能を付け加えたクラスです。
 * <p>このクラスはライブラリの内部でのみ利用することを想定しているため{@link Reader}を継承せず、
 * 最小限のメンバーを宣言・実装・公開するだけに留めています。</p>
 */
final class PeekReader implements Closeable, AutoCloseable{
	private final Reader _inner;
	private int _cache = -1;
	PeekReader(Reader inner) {
		ParsecUtility.mustNotBeNull("inner", inner);
		_inner = inner;
		try {
			_cache = inner.read();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	public final int peek() {
		return _cache;
	}
	public final int read() throws IOException {
		final int tmp = _cache;
		_cache = _inner.read();
		return tmp;
	}
	@Override
	public final void close() throws IOException {
		_inner.close();
	}
}