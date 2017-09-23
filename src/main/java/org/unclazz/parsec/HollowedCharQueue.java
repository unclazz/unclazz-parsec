package org.unclazz.parsec;

import java.util.NoSuchElementException;

/**
 * 中間部の要素を保持しない文字キューです。
 * <p>このキューは最初と最後の文字のみ格納しています。</p>
 */
final class HollowedCharQueue {
	private int _first = -1;
	private int _last = -1;
	/**
	 * キューを空にします。
	 */
	public void clear() {
		_first = -1;
		_last = -1;
	}
	/**
	 * 要素を追加します。
	 * @param ch
	 */
	public void push(char ch) {
		if (_first == -1) _first = ch;
		else _last = ch;
	}
	/**
	 * 最初の要素を返します。
	 * @return
	 * @throws NoSuchElementException キューが空である場合
	 */
	public char peekFirst() {
		if (_first == -1) throw new NoSuchElementException("queue is empty.");
		return (char)_first;
	}
	/**
	 * 最後の要素を返します。
	 * @return
	 * @throws NoSuchElementException キューが空である場合
	 */
	public char peekLast() {
		if (_first == -1) throw new NoSuchElementException("queue is empty.");
		return (char)(_last == -1 ? _first : _last);
	}
	/**
	 * キューの長さを返します。
	 * このキューは中間部の要素を持たないため長さは0から2のいずれかとなります。
	 * @return
	 */
	public int size() {
		return _first == -1 ? 0 : (_last == -1 ? 1 : 2);
	}
	/**
	 * キューが空かどうか判定します。
	 * @return
	 */
	public boolean isEmpty() {
		return _first == -1;
	}
}