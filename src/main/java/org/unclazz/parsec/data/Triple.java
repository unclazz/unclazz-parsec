package org.unclazz.parsec.data;

/**
/**
 * 要素数2のタプルです。
 *
 * @param <T1> 要素1の型
 * @param <T2> 要素2の型
 * @param <T3> 要素3の型
 */
public final class Triple<T1,T2, T3> implements Tuple {
	/**
	/**
	 * タプルのインスタンスを返します。
	 * @param item1 要素1の値
	 * @param item2 要素2の値
	 * @param item3 要素3の値
	 * @return
	 */
	public static<T1, T2, T3> Triple<T1, T2, T3> of(T1 item1, T2 item2, T3 item3){
		return new Triple<>(item1, item2, item3);
	}
	
	private final T1 _item1;
	private final T2 _item2;
	private final T3 _item3;
	
	private Triple(T1 item1, T2 item2, T3 item3) {
		_item1 = item1;
		_item2 = item2;
		_item3 = item3;
	}
	
	/**
	 * タプルの要素1です。
	 * @return
	 */
	public T1 item1() {
		return _item1;
	}
	/**
	 * タプルの要素2です。
	 * @return
	 */
	public T2 item2() {
		return _item2;
	}
	/**
	 * タプルの要素3です。
	 * @return
	 */
	public T3 item3() {
		return _item3;
	}
	@Override
	public int size() {
		return 3;
	}
	@Override
	public Object[] values() {
		return new Object[] { _item1, _item2, _item3 };
	}
}