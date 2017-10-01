package org.unclazz.parsec.util;

/**
 * タプルを表すインターフェースです。
 */
public interface Tuple {
	/**
	 * タプルのインスタンスを返します。
	 * @param item1 要素1の値
	 * @param item2 要素2の値
	 * @return タプル
	 * @param <T1> 要素1の型
	 * @param <T2> 要素2の型
	 */
	public static<T1, T2> Tuple2<T1, T2> of(T1 item1, T2 item2) {
		return new Tuple2<>(item1, item2);
	}
	/**
	 * タプルのインスタンスを返します。
	 * @param item1 要素1の値
	 * @param item2 要素2の値
	 * @param item3 要素3の値
	 * @return タプル
	 * @param <T1> 要素1の型
	 * @param <T2> 要素2の型
	 * @param <T3> 要素3の型
	 */
	public static<T1, T2, T3> Tuple3<T1, T2, T3> of(T1 item1, T2 item2, T3 item3){
		return new Tuple3<>(item1, item2, item3);
	}
	
	/**
	 * タプルの要素数です。
	 * @return タプルの要素数
	 */
	public int size();
	/**
	 * タプルのすべての要素をまとめて返します。
	 * @return タプルの要素の配列
	 */
	public Object[] values();
}
