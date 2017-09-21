package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

class PrependableReader extends AbstractReader {

	private CharArrayReader _prefixReader;
	private final PeekableReader _mainReader;
	
	PrependableReader(CharPosition p, char[] prefix, Reader main){
		position(p);
		_prefixReader = CharArrayReader.from(prefix);
		_mainReader = new PeekableReader(main);
	}
	PrependableReader(Reader main){
		_prefixReader = CharArrayReader.from(new char[0]);
		_mainReader = new PeekableReader(main);
	}
	@Override
	protected Closeable closable() {
		return _mainReader;
	}
	@Override
	protected int readSimply() throws IOException {
		return _prefixReader.hasReachedEof() ? _mainReader.read() : _prefixReader.read();
	}
	@Override
	public int peek() {
		return _prefixReader.hasReachedEof() ? _mainReader.peek() : _prefixReader.peek();
	}
	protected void reattach(CharPosition position, char[] prefix) {
		position(position);
		_prefixReader = CharArrayReader.from(prefix);
	}
}
