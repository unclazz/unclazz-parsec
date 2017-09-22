package org.unclazz.parsec;

/**
 * 文字の範囲を表すクラスです。
 */
final class CharRange{
	public final char start;
	public final char end;
	
	CharRange(char start, char end) {
		this.start = start < end ? start : end;
		this.end = start < end ? end : start;
	}
	
	/**
	 * 文字がこの範囲に含まれるかどうかを判定します。
	 * @param ch
	 * @return
	 */
	public boolean contains(int ch) {
		return start <= ch && ch <= end;
	}
	@Override
	public String toString() {
		if (start == end) return ParsecUtility.charToString(start);
		return String.format("%s to %s", 
				ParsecUtility.charToString(start),
				ParsecUtility.charToString(end));
	}
}