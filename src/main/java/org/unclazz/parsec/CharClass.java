package org.unclazz.parsec;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

public abstract class CharClass {
	public static CharClass between(char start, char end) {
		return new CharRangeCharClass(new CharRange[]{ new CharRange(start, end) });
	}
	public static CharClass exact(char ch) {
		return new ExactCharCharClass(ch);
	}
	public static CharClass anyOf(char...cs) {
		return new CharRangeCharClass(cs);
	}
	
	public abstract boolean contains(int ch);
	
	public CharClass union(CharClass other) {
		return new UnionCharClass(this, other);
	}
	public CharClass plus(char ch) {
		return new UnionCharClass(this, exact(ch));
	}
}

final class UnionCharClass extends CharClass{
	private final CharClass _left;
	private final CharClass _right;
	
	public UnionCharClass(CharClass left, CharClass right) {
		ParsecUtility.mustNotBeNull("left", left);
		ParsecUtility.mustNotBeNull("right", right);
		_left = left;
		_right = right;
	}
	@Override
	public boolean contains(int ch) {
		return _left.contains(ch) || _right.contains(ch);
	}
}

final class ExactCharCharClass extends CharClass{
	private char _ch;
	public ExactCharCharClass(char ch) {
		_ch = ch;
	}
	@Override
	public boolean contains(int ch) {
		return _ch == ch;
	}
	@Override
	public CharClass union(CharClass other) {
		if (other.contains(_ch)) return other;
		return super.union(other);
	}
	@Override
	public CharClass plus(char ch) {
		if (_ch == ch) return this;
		return super.plus(ch);
	}
}
final class CharRangeCharClass extends CharClass{
	private final CharRange[] _charRanges;
	
	CharRangeCharClass(CharRange[] ranges) {
		_charRanges = ranges;
	}
	CharRangeCharClass(char[] cs) {
		_charRanges = makeRanges(cs);
	}
	
	@Override
	public boolean contains(int ch) {
		for (int i = 0; i < _charRanges.length; i++) {
			if (_charRanges[i].contains(ch)) return true;
		}
		return false;
	}
	@Override
	public CharClass union(CharClass other) {
		if (other instanceof CharRangeCharClass) {
			final CharRangeCharClass that = (CharRangeCharClass) other;
			return new CharRangeCharClass(tryMerge(concat(_charRanges, that._charRanges)));
		}
		return super.union(other);
	}
	@Override
	public CharClass plus(char ch) {
		if (contains(ch)) return this;
		return super.plus(ch);
	}
	
	private static CharRange[] concat(CharRange[] left, CharRange[] right) {
		final CharRange[] tmp = Arrays.copyOf(left, left.length + right.length);
		System.arraycopy(right, 0, tmp, left.length, right.length);
		return tmp;
	}
	private static char[] distinct(char[] cs) {
		if (cs.length <= 1) return cs;
		Arrays.sort(cs);
		final char[] tmp = new char[cs.length];
		int size = 0;
		if (cs[0] == '\0') size ++;
		
		for (int i = size; i < cs.length; i ++) {
			final char ch = cs[i];
			if (Arrays.binarySearch(tmp, ch) != -1) {
				cs[size ++] = ch;
			}
		}
		return Arrays.copyOf(tmp, size);
	}
	static CharRange[] makeRanges(char[] cs) {
		if (cs.length == 0) return new CharRange[0];
		
		final char[] uniq = distinct(cs);
		if (uniq.length == 1) return new CharRange[] { new CharRange((char)uniq[0], (char)uniq[0]) };
		
        // 文字の範囲を構成するメンバを一時的に格納するバッファ
		final Queue<CharRange> rangesBuff = new LinkedList<>();
        // 文字の範囲を表すオブジェクトを一時的に格納するバッファ
		final CharStack charsBuff = new CharStack(100);
		
		for (int i = 0; i < uniq.length; i ++) {
			final char curr = uniq[i];
			final char prev = i == 0 ? uniq[0] : uniq[i - 1];
			final int increment = curr - prev;
			
            // コード差分が1より大きい＝文字の範囲の分断が生じる
			if (increment > 1) {
                // 範囲メンバを格納しているバッファの内容をもとに範囲オブジェクトを生成
                // 範囲オブジェクト用のバッファに追加
				rangesBuff.add(new CharRange(charsBuff.peekFirst(), charsBuff.peek()));
                // 範囲メンバを格納しているバッファはクリア
				charsBuff.clear();
			}
            // 文字を文字範囲メンバ用のバッファに追加
			charsBuff.push(curr);
		}
		
		if (charsBuff.size() > 0) {
			rangesBuff.add(new CharRange(charsBuff.peekFirst(), charsBuff.peek()));
		}
		
		return rangesBuff.toArray(new CharRange[0]);
	}
	static CharRange[] tryMerge(CharRange[] rs) {
		if (rs.length <= 1) return rs;
		return Arrays.stream(rs).sorted(CharRangeCharClass::compare2Ranges)
				.reduce(new Stack<CharRange>(), CharRangeCharClass::tryMerge_accumulator, 
						CharRangeCharClass::tryMerge_combinator).toArray(new CharRange[0]);
	}
	static int compare2Ranges(CharRange left, CharRange right) {
		return left.start - right.end;
	}
	static Stack<CharRange> tryMerge_combinator(Stack<CharRange> a, Stack<CharRange> b) {
		a.addAll(b);
		return a;
	}
	static Stack<CharRange> tryMerge_accumulator(Stack<CharRange> stack, CharRange range) {
        if (stack.isEmpty()) {
            stack.push(range);
            return stack;
        }
        final Optional<CharRange> res = tryMerge_accumulator_merge2Ranges(stack.peek(), range);
        if (res.isPresent()) {
            stack.pop();
            stack.push(res.get());
        } else {
            stack.push(range);
        }
        return stack;
	}
	static Optional<CharRange> tryMerge_accumulator_merge2Ranges(CharRange left, CharRange right) {
		if (/* left.start <= right.start && */ right.start <= left.end) {
			if (left.end <= right.end || (right.end - left.end) == 1) {
                return Optional.of(new CharRange(left.start, right.end));
            }
            return Optional.of(left);
		}
		return Optional.empty();
	}
}

final class ComplementCharClass extends CharClass{
	private final CharClass _clazz;
	public ComplementCharClass(CharClass clazz) {
		ParsecUtility.mustNotBeNull("clazz", clazz);
		_clazz = clazz;
	}
	@Override
	public boolean contains(int ch) {
		return !_clazz.contains(ch);
	}
}

