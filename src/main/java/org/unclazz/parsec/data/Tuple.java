package org.unclazz.parsec.data;

/**
 * タプルを表すインターフェースです。
 */
public interface Tuple {
	/**
	 * タプルの要素数です。
	 * @return
	 */
	public int size();
	/**
	 * タプルのすべての要素をまとめて返します。
	 * @return
	 */
	public Object[] values();
}
