package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * データソースの先頭にシーケンスを接続する機能を持つリーダーです。
 */
class PrefixReader extends AbstractReader {

	private CharArrayReader _prefixCharArray;
	private final PeekReader _mainReader;
	
	PrefixReader(CharPosition p, char[] prefix, Reader main){
		ParsecUtility.mustNotBeNull("p", p);
		ParsecUtility.mustNotBeNull("prefix", prefix);
		ParsecUtility.mustNotBeNull("main", main);
		position(p);
		_prefixCharArray = CharArrayReader.from(prefix);
		_mainReader = new PeekReader(main);
	}
	PrefixReader(Reader main){
		ParsecUtility.mustNotBeNull("main", main);
		_prefixCharArray = CharArrayReader.from(new char[0]);
		_mainReader = new PeekReader(main);
	}
	@Override
	protected final Closeable closable() {
		return _mainReader;
	}
	@Override
	protected int readOne() throws IOException {
		return _prefixCharArray.noRemaining() ? _mainReader.read() : _prefixCharArray.read();
	}
	@Override
	public final int peek() {
		return _prefixCharArray.noRemaining() ? _mainReader.peek() : _prefixCharArray.peek();
	}
	/**
	 * 文字位置を設定し直し、データソースの先頭にシーケンスを連結します。
	 * @param position 文字位置
	 * @param prefix データソースの先頭に連結するシーケンス
	 */
	protected final void reattach(CharPosition position, char[] prefix) {
		position(position);
		_prefixCharArray = _prefixCharArray.prepend(prefix);
	}
}
