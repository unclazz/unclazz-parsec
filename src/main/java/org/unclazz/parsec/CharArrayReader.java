package org.unclazz.parsec;

import java.nio.CharBuffer;
import java.util.Arrays;

final class CharArrayReader {
	private static final CharArrayReader _empty = new CharArrayReader(new char[0]);
	private final char[] _items;
	private int _index;
	
	private CharArrayReader(char[] array){
		_items = array;
	}
	
	public static CharArrayReader from(char[] items){
		ParsecUtility.mustNotBeNull("items", items);
		if (items.length == 0) return _empty;
		return new CharArrayReader(Arrays.copyOf(items, items.length));
	}
	
	public boolean hasReachedEof(){
		return _items.length <= _index;
	}
	public int size() {
		return _items.length - _index;
	}
	public int read(){
		return _index < _items.length ? _items[_index++] : -1;
	}
	public String readToEnd() {
		if (hasReachedEof()) return null;
		final int len = _items.length - _index;
		final CharBuffer buf = CharBuffer.allocate(len).put(_items, _index, len);
		_index = _items.length;
		return buf.flip().toString();
	}
	public int peek(){
		return _index < _items.length ? _items[_index] : -1;
	}
	public CharArrayReader prepend(char[] prefix){
		ParsecUtility.mustNotBeNull("prefix", prefix);
		
		if (prefix.length == 0) return this;
		if (hasReachedEof()) return new CharArrayReader(prefix);
		
		final int restLength = _items.length - _index;
		final int newLength = prefix.length + restLength;
		final char[] newItems = Arrays.copyOf(prefix, newLength);
		for (int i = prefix.length, j = _index; i < newLength; i++, j++){
			newItems[i] = _items[j];
		}
		
		return new CharArrayReader(newItems);
	}
	public char[] toArray(){
		final int restLength = _items.length - _index;
		final char[] arr = new char[restLength];
		for (int i = 0, j = _index; i < restLength; i++, j++){
			arr[i] = _items[j];
		}
		return arr;
	}
}
