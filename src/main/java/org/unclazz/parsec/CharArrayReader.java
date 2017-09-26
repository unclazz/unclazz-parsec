package org.unclazz.parsec;

import java.io.Reader;
import java.util.Arrays;

/**
 * 文字の配列をデータソースとするリーダーです。
 * <p>データソースの先頭に別の文字の配列を連結する機能をサポートしており、
 * {@link PrependableReader}の実装コードで利用されています。</p>
 * <p>このクラスはライブラリの内部でのみ利用することを想定しているため{@link Reader}を継承せず、
 * 最小限のメンバーを宣言・実装・公開するだけに留めています。</p>
 */
final class CharArrayReader {
	private static final CharArrayReader _empty = new CharArrayReader(new char[0]);
	
	/**
	 * 文字の配列をもとに新しいインスタンスを生成します。
	 * @param items
	 * @return
	 */
	public static CharArrayReader from(char[] items){
		ParsecUtility.mustNotBeNull("items", items);
		if (items.length == 0) return _empty;
		return new CharArrayReader(Arrays.copyOf(items, items.length));
	}
	
	private final char[] _items;
	private int _index;
	
	private CharArrayReader(char[] array){
		_items = array;
	}
	
	/**
	 * データソースのEOFに到達しているかどうか判定します。
	 * @return
	 */
	public boolean noRemaining(){
		return _items.length <= _index;
	}
	/**
	 * データソースから読み取れる残りの文字数です。
	 * @return
	 */
	public int size() {
		return _items.length - _index;
	}
	/**
	 * データソースから1文字読み取り文字位置を前進させます。
	 * @return
	 */
	public int read(){
		return _index < _items.length ? _items[_index++] : -1;
	}
	/**
	 * データソースから現在の文字位置以降のすべての文字を読み取ります。
	 * @return
	 */
	public String readToEnd() {
		if (noRemaining()) return null;
		final String tmp = new String(_items, _index, _items.length - _index);
		_index = _items.length;
		return tmp;
	}
	/**
	 * 現在の文字位置の文字を返します。
	 * @return
	 */
	public int peek(){
		return _index < _items.length ? _items[_index] : -1;
	}
	/**
	 * データソースの先頭に別の文字配列を連結した新しいリーダーを返します。
	 * @param prefix 
	 * @return
	 */
	public CharArrayReader prepend(char[] prefix){
		ParsecUtility.mustNotBeNull("prefix", prefix);
		
		if (prefix.length == 0) return this;
		if (noRemaining()) return new CharArrayReader(prefix);
		
		final int restLength = _items.length - _index;
		final int newLength = prefix.length + restLength;
		final char[] newItems = Arrays.copyOf(prefix, newLength);
		
		System.arraycopy(_items, _index, newItems, prefix.length, restLength);
		return new CharArrayReader(newItems);
	}
	/**
	 * データソースから現在の文字位置以降のすべての文字を読み取り配列として返します。
	 * @return
	 */
	public char[] toArray(){
		final int restLength = _items.length - _index;
		final char[] arr = new char[restLength];
		System.arraycopy(_items, _index, arr, 0, restLength);
		return arr;
	}
}
