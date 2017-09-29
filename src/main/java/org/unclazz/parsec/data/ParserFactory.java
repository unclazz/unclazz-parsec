package org.unclazz.parsec.data;

import org.unclazz.parsec.Parser;

/**
 * {@link Parser}オブジェクトのファクトリーを表すインターフェースです。
 */
@FunctionalInterface
public interface ParserFactory {
	/**
	 * パーサーを返します。
	 * @return
	 */
	Parser create();
}
