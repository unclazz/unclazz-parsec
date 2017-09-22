package org.unclazz.parsec;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 文字のスタックを表すクラスです。
 * {@link PrependableReader}内部で利用されています。
 */
final class CharStack {
	private int _size;
	private char[] _items;
	
	CharStack(int unit){
		ParsecUtility.mustBeGreaterThan("uni", unit, 0);
		_items = new char[unit];
		_size = 0;
	}
	
	/**
	 * スタックに新しい要素を追加します。
	 * @param item
	 */
	public void push(char item) {
		_size++;
		if (_size > _items.length) {
			_items = Arrays.copyOf(_items, _items.length * 2);
		}
		_items[_size - 1] = item;
	}
	/**
	 * スタックから指定した数の要素を取り出します。
	 * @param size
	 * @return
	 */
	public char[] pop(int size) {
		checkIndex(size);
		if (size == 0) return new char[0];
		if (size == _size) return popAll();
		
		final char[] result = new char[size];
		final int newSize = _size - size;
		System.arraycopy(_items, newSize, result, 0, size);
		_size = newSize;
		return result;
	}
	/**
	 * スタックからすべての要素を取り出します。
	 * @return
	 */
	public char[] popAll() {
		final char[] tmp = toArray();
		clear();
		return tmp;
	}
	/**
	 * スタックの最上部の文字を返します。
	 * @return
	 */
	public char peek() {
		if (_size < 1) throw new NoSuchElementException();
		return _items[_size - 1];
	}
	/**
	 * スタックの最下部の文字を返します。
	 * @return
	 */
	public char peekFirst() {
		if (_size < 1) throw new NoSuchElementException();
		return _items[0];
	}
	/**
	 * スタックの要素数です。
	 * @return
	 */
	public int size() {
		return _size;
	}
	/**
	 * スタックの内容を配列として返します。
	 * @param from
	 * @return
	 */
	public char[] toArray(int from) {
		checkIndex(from);
		
		return Arrays.copyOfRange(_items, from, _size);
	}
	/**
	 * スタックの内容を配列として返します。
	 * @return
	 */
	public char[] toArray() {
		return toArray(0);
	}
	/**
	 * スタックの指定した位置以上の要素からなる文字列を返します。
	 * @param from
	 * @return
	 */
	public String toString(int from) {
		checkIndex(from);
		final int len = _size - from;
		if (len == 0) return "";
		return CharBuffer.allocate(len).put(_items, from, len).flip().toString();
	}
	@Override
	public String toString() {
		return toString(0);
	}
	/**
	 * スタックを空にします。
	 */
	public void clear() {
		_size = 0;
	}
	private void checkIndex(int i) {
		if (i < 0) {
			throw new IndexOutOfBoundsException(String.format
					("index (%s) must be greater than or equal 0.", i));
		}
		if (_size < i) {
			throw new IllegalArgumentException(String.format
					("index (%s) is too large for this CharStack (size=%s).",
							i, _size));
		}
	}
}