package org.unclazz.parsec.util;

import org.unclazz.parsec.ValParser;

/**
 * {@link ValParser}オブジェクトのファクトリーを表す関数型インターフェースです。
 * @param <T> 読み取り結果型
 */
@FunctionalInterface
public interface ValParserFactory<T> {
	/**
	 * パーサーを返します。
	 * @return パーサー
	 */
	ValParser<T> create();
}
