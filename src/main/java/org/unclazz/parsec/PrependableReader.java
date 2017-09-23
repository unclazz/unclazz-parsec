package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * データソースの先頭にシーケンスを接続する機能を持つリーダーです。
 */
class PrependableReader extends AbstractReader {

	private CharArrayReader _prefixReader;
	private final PeekableReader _mainReader;
	
	PrependableReader(CharPosition p, char[] prefix, Reader main){
		ParsecUtility.mustNotBeNull("p", p);
		ParsecUtility.mustNotBeNull("prefix", prefix);
		ParsecUtility.mustNotBeNull("main", main);
		position(p);
		_prefixReader = CharArrayReader.from(prefix);
		_mainReader = new PeekableReader(main);
	}
	PrependableReader(Reader main){
		ParsecUtility.mustNotBeNull("main", main);
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
	/**
	 * 文字位置を設定し直し、データソースの先頭にシーケンスを連結します。
	 * @param position 文字位置
	 * @param prefix データソースの先頭に連結するシーケンス
	 */
	protected void reattach(CharPosition position, char[] prefix) {
		position(position);
		_prefixReader = _prefixReader.prepend(prefix);
	}
}
