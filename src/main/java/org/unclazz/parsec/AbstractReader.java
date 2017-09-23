package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;

/**
 * リーダーを表す抽象クラスです。
 */
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
	
	/**
	 * データソースの参照を返します。
	 * @return
	 */
	protected abstract Closeable closable();
	/**
	 * データソースから1文字を読み取り文字位置を前進させます。
	 * @return 
	 * @throws IOException 読み取り中に例外がスローされた場合
	 */
	protected abstract int readSimply() throws IOException;
	/**
	 * 現在の文字位置が指す文字を返します。
	 * @return
	 */
	public abstract int peek();
	
	/**
	 * 新しい文字位置を設定します。
	 * @param position
	 */
	protected void position(CharPosition position) {
		ParsecUtility.mustNotBeNull("position", position);
		_position = position;
	}
	/**
	 * 現在の文字位置を返します。
	 * @return
	 */
	public CharPosition position() {
		return _position;
	}
	/**
	 * EOFに到達しているかどうか判定します。
	 * @return
	 */
	public boolean hasReachedEof() {
		return peek() == -1;
	}
	/**
	 * データソースから1文字読み取り文字位置を前進させます。
	 * EOFに到達している場合は{@code -1}を返します。
	 * @return
	 * @throws IOException 読み取り中に例外がスローされた場合
	 */
	public int read() throws IOException {
		// 派生クラスに純粋な読み取り操作を行わせる
		final int ch = readSimply();
		// 読み取られた文字をチェック
		if (ch == '\n' || ch == '\r' && peek() != '\n') {
			// LFもしくはCR（ただしLFを伴わない）である場合
			// 改行が発生したと見なして文字位置を更新
			_position = _position.nextLine();
		}else if (ch != -1) {
			// EOFでなければ1カラム移動したと見なして文字位置を更新
			_position = _position.nextColumn();
		}
		// 読み取られた文字を呼び出し元に返す
		return ch;
	}
	/**
	 * 現在の文字位置から行末までの文字列を読み取ります。
	 * @return 
	 * @throws IOException 文字の読み取り中に例外がスローされた場合
	 */
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
	/**
	 * 現在の文字位置からEOFまでの文字列を読み取ります。
	 * @return
	 * @throws IOException 文字の読み取り中に例外がスローされた場合
	 */
	public String readToEnd() throws IOException {
		if (hasReachedEof()) return null;
		final StringBuilder buff = new StringBuilder();
		while (!hasReachedEof()) buff.append((char)read());
		return buff.toString();
	}
	/**
	 * データソースをクローズします。
	 */
	@Override
	public void close() throws IOException {
		final Closeable c = closable();
		if (c != null) c.close();
	}
}
