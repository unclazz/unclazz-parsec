package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

final class PeekableReader implements Closeable, AutoCloseable{
	private final Reader _inner;
	private int _cache = -1;
	public PeekableReader(Reader inner) {
		ParsecUtility.mustNotBeNull("inner", inner);
		_inner = inner;
		try {
			_cache = inner.read();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	public int peek() {
		return _cache;
	}
	public int read() throws IOException {
		final int tmp = _cache;
		_cache = _inner.read();
		return tmp;
	}
	@Override
	public void close() throws IOException {
		_inner.close();
	}
}