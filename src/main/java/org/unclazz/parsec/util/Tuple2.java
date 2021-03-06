package org.unclazz.parsec.util;

/**
 * 要素数2のタプルです。
 *
 * @param <T1> 要素1の型
 * @param <T2> 要素2の型
 */
public final class Tuple2<T1,T2> implements Tuple {
	private final T1 _item1;
	private final T2 _item2;
	
	Tuple2(T1 item1, T2 item2) {
		_item1 = item1;
		_item2 = item2;
	}
	
	/**
	 * タプルの要素1です。
	 * @return タプル要素
	 */
	public T1 item1() {
		return _item1;
	}
	/**
	 * タプルの要素2です。
	 * @return タプル要素
	 */
	public T2 item2() {
		return _item2;
	}
	@Override
	public int size() {
		return 2;
	}
	@Override
	public Object[] values() {
		return new Object[] { _item1, _item2 };
	}
	@Override
	public String toString() {
		return String.format("(%s, %s)", _item1, _item2);
	}
}