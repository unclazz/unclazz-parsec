package org.unclazz.parsec;

final class CharRange{
	public final char start;
	public final char end;
	
	public CharRange(char start, char end) {
		this.start = start < end ? start : end;
		this.end = start < end ? end : start;
	}
	
	public boolean contains(int ch) {
		return start <= ch && ch <= end;
	}
	public String toString() {
		if (start == end) return ParsecUtility.charToString(start);
		return String.format("%s to %s", 
				ParsecUtility.charToString(start),
				ParsecUtility.charToString(end));
	}
}