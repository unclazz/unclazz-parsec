package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;

abstract class AbstractReader implements Closeable, AutoCloseable {
	@SuppressWarnings("unused")
	private final Object finalizerGuardian = new Object() {
        @Override
        protected void finalize() throws Throwable {
            final Closeable c = closable();
            if (c != null) c.close();
        }
    };
	private CharPosition _position = CharPosition.ofBof();
	
	protected abstract Closeable closable();
	protected abstract int readSimply() throws IOException;
	public abstract int peek();
	
	protected void position(CharPosition position) {
		ParsecUtility.mustNotBeNull("position", position);
		_position = position;
	}
	public CharPosition position() {
		return _position;
	}
	public boolean hasReachedEof() {
		return peek() == -1;
	}
	public int read() throws IOException {
		final int ch = readSimply();
		if (ch == '\n' || ch == '\r' && peek() != '\n') {
			_position = _position.nextLine();
		}else if (ch != -1) {
			_position = _position.nextColumn();
		}
		return ch;
	}
	public String readLine() throws IOException {
		if (hasReachedEof()) return null;
		final int startedOn = _position.line();
		final StringBuilder buff = new StringBuilder();
		while (startedOn == _position.line() && !hasReachedEof()) {
			int ch = read();
			if (ch != '\r' && ch != '\n') buff.append((char)ch);
		}
		return buff.toString();
	}
	public String readToEnd() throws IOException {
		if (hasReachedEof()) return null;
		final StringBuilder buff = new StringBuilder();
		while (!hasReachedEof()) buff.append((char)read());
		return buff.toString();
	}
	public void close() throws IOException {
		final Closeable c = closable();
		if (c != null) c.close();
	}
}
