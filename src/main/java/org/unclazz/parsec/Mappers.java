package org.unclazz.parsec;

import java.nio.CharBuffer;

import org.unclazz.parsec.data.Tuple2;
import org.unclazz.parsec.data.Tuple3;

public final class Mappers {
	private Mappers() {}
	private static final JavaStringUnescaper _javaString = new JavaStringUnescaper();
	private static final JsonStringUnescaper _jsonString = new JsonStringUnescaper();
	public static String javaString(String value) {
		return _javaString.unescape(value);
	}
	public static String jsonString(String value) {
		return _jsonString.unescape(value);
	}
	public static Integer digits(String value) {
		return Integer.parseInt(value);
	}
	public static Integer octalDigits(String value) {
		return Integer.parseInt(value, 8);
	}
	public static Integer hexDigits(String value) {
		return Integer.parseInt(value, 16);
	}
	public static Double floatingPoint(String value) {
		return Double.parseDouble(value);
	}
	public static<T1, T2, T3> Tuple3<T1, T2, T3> flatten(Tuple2<Tuple2<T1, T2>, T3> tuple) {
		return Tuple3.of(tuple.item1().item1(), tuple.item1().item2(), tuple.item2());
	}
}


final class JavaStringUnescaper{
	public String unescape(String original) {
		final CharBuffer rBuff = CharBuffer.wrap(original);
		final CharBuffer wBuff = CharBuffer.allocate(original.length());
		
		while (rBuff.hasRemaining()) {
			final char ch = rBuff.get();
			if (ch == '\\') {
				final char ch2 = rBuff.get();
				if (ch2 == 'u') {
					wBuff.put(hex4Digits(rBuff));
				} else if ('0' <= ch2 && ch2 <= '7') {
					rBuff.position(rBuff.position() - 1);
					wBuff.put(octal3Digits(rBuff));
				} else {
					wBuff.put(control(ch2));
				}
			} else {
				wBuff.put(ch);
			}
		}
		return wBuff.flip().toString();
	}
	
	private char octal3Digits(CharBuffer rBuff) {
		final int o0 = octalDigit(rBuff.get());
		final int o1 = octalDigit(rBuff.get());
		if (o1 == -1) {
			if (o0 > 3) throw new IllegalArgumentException(
					String.format("[0-3] expected but %s found.",
							ParsecUtility.charToString(o0)));
			rBuff.position(rBuff.position() - 1);
			return (char)o0;
		}
		final int o2 = octalDigit(rBuff.get());
		if (o2 == -1) {
			rBuff.position(rBuff.position() - 1);
			return (char)(o0 * 8 + o1);
		}
		return (char)(o0 * 64 + o1 * 8 + o2);
	}
	private int octalDigit(char ch) {
		if ('0' <= ch && ch <= '8') {
			return ch - '0';
		} else {
			return -1;
		}
	}
	private char hex4Digits(CharBuffer rBuff) {
		return (char)(hexDigit(rBuff.get()) * (16 * 16 * 16) 
				+ hexDigit(rBuff.get()) * (16 * 16) 
				+ hexDigit(rBuff.get()) * 16
				+ hexDigit(rBuff.get()));
	}
	private int hexDigit(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		} else if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		} else if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		} else {
			throw new IllegalArgumentException(
					String.format("[0-9A-Za-z] expected but %s found.",
							ParsecUtility.charToString(ch)));
		}
	}
	private char control(char ch) {
		switch (ch) {
		case 'b':
			return '\b';
		case 't':
			return '\t';
		case 'n':
			return '\n';
		case 'f':
			return '\f';
		case 'r':
			return '\r';
		case '"':
			return '"';
		case '\'':
			return '\'';
		case '\\':
			return '\\';
		default:
			throw new IllegalArgumentException(
					String.format("unknown escape sequence '\\' + %s found.",
							ParsecUtility.charToString(ch)));
		}
	}
}
final class JsonStringUnescaper{
	public String unescape(String original) {
		final CharBuffer rBuff = CharBuffer.wrap(original);
		final CharBuffer wBuff = CharBuffer.allocate(original.length());
		
		while (rBuff.hasRemaining()) {
			final char ch = rBuff.get();
			if (ch == '\\') {
				final char ch2 = rBuff.get();
				if (ch2 == 'u') {
					wBuff.put(hex4Digits(rBuff));
				} else {
					wBuff.put(control(ch2));
				}
			} else {
				wBuff.put(ch);
			}
		}
		return wBuff.flip().toString();
	}
	private char hex4Digits(CharBuffer rBuff) {
		return (char)(hexDigit(rBuff.get()) * (16 * 16 * 16) 
				+ hexDigit(rBuff.get()) * (16 * 16) 
				+ hexDigit(rBuff.get()) * 16
				+ hexDigit(rBuff.get()));
	}
	private int hexDigit(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		} else if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		} else if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		} else {
			throw new IllegalArgumentException(
					String.format("[0-9A-Za-z] expected but %s found.",
							ParsecUtility.charToString(ch)));
		}
	}
	private char control(char ch) {
		switch (ch) {
		case '"':
			return '"';
		case '\'':
			return '\'';
		case '\\':
			return '\\';
		case 'b':
			return '\b';
		case 'f':
			return '\f';
		case 'n':
			return '\n';
		case 'r':
			return '\r';
		case 't':
			return '\t';
		default:
			throw new IllegalArgumentException(
					String.format("unknown escape sequence '\\' + %s found.",
							ParsecUtility.charToString(ch)));
		}
	}
}