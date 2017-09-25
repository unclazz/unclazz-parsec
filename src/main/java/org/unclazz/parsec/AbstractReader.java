package org.unclazz.parsec;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * リーダーを表す抽象クラスです。
 * <p>このクラスは派生型に対して次の機能を提供します：</p>
 * <ul>
 * 	<li>現在の文字位置について行・列・インデクスの3値の参照と更新機能（{@link #position()}）</li>
 * 	<li>データソースの終端（EOF）に到達したかどうかのチェックするメソッド（{@link #hasReachedEof()}）</li>
 * 	<li>1行分のテキストやデータソースの終端までのテキストの一括読み取り機能（{@link #readLine()}と{@link #readToEnd()}）</li>
 * 	<li>ストリームなどの自動クローズとリソースの解放（{@link #close()}メソッド呼び出し時もしくはファイナライズ時）</li>
 * </ul>
 * <p>これらの機能のため派生型は次の抽象メソッドを実装する必要があります：</p>
 * <ul>
 * <li>{@link #readOne()} - 派生型はこのメソッドを実装することでその固有の仕様に基づきデータソースのコンテンツの読み取りと
 * それに付随するオペレーションを行います。このメソッドは抽象クラスの{@link #read()}の内部で呼び出されます。</li>
 * <li>{@link #peek()} - 派生型はこのメソッドを実装することで次に{@link #read()}すべきコンテンツがデータソースに残っているか示します。</li>
 * <li>{@link #closable()} - 派生型はこのメソッドを実装することで自動クローズすべき対象を示します。</li>
 * </ul>
 */
abstract class AbstractReader extends java.io.Reader implements Closeable, AutoCloseable {
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
	 * <p>このメソッドは{@link Reader#read()}の規約に準拠します。</p>
	 * @return 
	 * @throws IOException 読み取り中に例外がスローされた場合
	 */
	protected abstract int readOne() throws IOException;
	/**
	 * 現在の文字位置が指す文字を返します。
	 * <p>このメソッドを呼び出す前にすでにデータソースの終端（EOF）の到達している場合は{@code -1}を返します。</p>
	 * @return
	 */
	public abstract int peek();
	
	/**
	 * 新しい文字位置を設定します。
	 * @param position
	 */
	protected final void position(CharPosition position) {
		ParsecUtility.mustNotBeNull("position", position);
		_position = position;
	}
	/**
	 * 現在の文字位置を返します。
	 * @return
	 */
	public final CharPosition position() {
		return _position;
	}
	/**
	 * EOFに到達しているかどうか判定します。
	 * @return
	 */
	public final boolean hasReachedEof() {
		return peek() == -1;
	}
	@Override
	public final int read() throws IOException {
		synchronized (this) {
			// 派生クラスに純粋な読み取り操作を行わせる
			final int ch = readOne();
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
	}
	@Override
	public final int read(char[] cbuf, int off, int len) throws IOException {
		synchronized (this) {
			if (hasReachedEof()) return -1;
			
			final int maxIndex = Math.min(cbuf.length, off + len);
			int charCount = 0;
			for (int i = off; i < maxIndex && !hasReachedEof(); i ++) {
				cbuf[i] = (char)read();
				charCount ++;
			}
			return charCount;
		}
	}
	/**
	 * 現在の文字位置から行末までの文字列を読み取ります。
	 * <p>行末はCRもしくはLF、CRLFにより判別されます。
	 * このメソッドが返す文字列は現在の文字位置からこれらの終端を示す文字の直前までの文字のシーケンスです。
	 * メソッドを呼び出す前にすでにデータソースのEOFまで到達している場合は{@code null}を返します。</p>
	 * @return 
	 * @throws IOException 文字の読み取り中に例外がスローされた場合
	 */
	public final String readLine() throws IOException {
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
	 * <p>このメソッドを呼び出す前にすでにデータソースのEOFまで到達している場合は{@code null}を返します。</p>
	 * @return
	 * @throws IOException 文字の読み取り中に例外がスローされた場合
	 */
	public final String readToEnd() throws IOException {
		if (hasReachedEof()) return null;
		final StringBuilder buff = new StringBuilder();
		while (!hasReachedEof()) buff.append((char)read());
		return buff.toString();
	}
	@Override
	public final void close() throws IOException {
		final Closeable c = closable();
		if (c != null) c.close();
	}
}
