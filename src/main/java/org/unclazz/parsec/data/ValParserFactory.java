package org.unclazz.parsec.data;

import org.unclazz.parsec.ValParser;

/**
 * {@link ValParser}オブジェクトのファクトリーを表す関数型インターフェースです。
 * @param <T>
 */
@FunctionalInterface
public interface ValParserFactory<T> {
	/**
	 * パーサーを返します。
	 * @return
	 */
	ValParser<T> create();
}
