package org.unclazz.parsec;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.NoSuchElementException;

final class CharStack {
	private int _size;
	private char[] _items;
	
	CharStack(int unit){
		ParsecUtility.mustBeGreaterThan("uni", unit, 0);
		_items = new char[unit];
		_size = 0;
	}
	
	public void push(char item) {
		_size++;
		if (_size > _items.length) {
			_items = Arrays.copyOf(_items, _items.length * 2);
		}
		_items[_size - 1] = item;
	}
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
	public char[] popAll() {
		final char[] tmp = toArray();
		clear();
		return tmp;
	}
	public char peek() {
		if (_size < 1) throw new NoSuchElementException();
		return _items[_size - 1];
	}
	public char peekFirst() {
		if (_size < 1) throw new NoSuchElementException();
		return _items[0];
	}
	public int size() {
		return _size;
	}
	public char[] toArray(int from) {
		checkIndex(from);
		
		return Arrays.copyOfRange(_items, from, _size);
	}
	public char[] toArray() {
		return toArray(0);
	}
	public String toString(int from) {
		checkIndex(from);
		final int len = _size - from;
		if (len == 0) return "";
		return CharBuffer.allocate(len).put(_items, from, len).flip().toString();
	}
	public String toString() {
		return toString(0);
	}
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