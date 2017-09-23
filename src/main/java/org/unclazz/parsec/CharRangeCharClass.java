package org.unclazz.parsec;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

/**
 * 範囲に基づく文字クラスです。
 */
final class CharRangeCharClass extends CharClass{
	private final CharRange[] _charRanges;
	
	CharRangeCharClass(CharRange[] ranges) {
		ParsecUtility.mustNotBeNull("ranges", ranges);
		_charRanges = ranges;
	}
	CharRangeCharClass(char[] cs) {
		ParsecUtility.mustNotBeNull("cs", cs);
		
		// 文字集合から範囲（1～N個）を生成する
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
	
	/**
	 * 2つの配列を連結して1つの配列にします。
	 * @param left
	 * @param right
	 * @return
	 */
	private static CharRange[] concat(CharRange[] left, CharRange[] right) {
		// 左辺をベースに新しい配列を生成
		final CharRange[] tmp = Arrays.copyOf(left, left.length + right.length);
		// 新しい配列の後部に右辺の要素をコピー
		System.arraycopy(right, 0, tmp, left.length, right.length);
		return tmp;
	}
	/**
	 * 重複を除去した新しい配列を作成します。
	 * @param cs
	 * @return
	 */
	private static char[] distinct(char[] cs) {
		// 長さが0もしくは1であれば重複はありえないのでそのまま返す
		if (cs.length <= 1) return cs;
		
		// ソートを行う
		Arrays.sort(cs);
		
		// バッファとなる配列を初期化
		final char[] buf = new char[cs.length];
		// バッファ内の有効な要素数を表す変数を初期化
		int size = 0;
		
		// 引数の文字配列（ソート後）の最初の要素がNULの場合
		// バッファの要素数をインクリメントだけする
		// ※バッファは初期化時に全要素がNULに設定されているから追加は不要
		if (cs[0] == '\0') size ++;
		
		// 引数の文字配列（ソート後）の要素ごとに処理
		for (int i = size; i < cs.length; i ++) {
			final char ch = cs[i];
			// バッファ内にすでに存在するかどうか判定する
			if (Arrays.binarySearch(buf, ch) == -1) {
				// 含まれていない場合はバッファに追加する
				buf[size ++] = ch;
			}
		}
		
		// バッファをもとに有効な要素のみからなる配列を生成して返す
		return Arrays.copyOf(buf, size);
	}
	/**
	 * 文字集合をもとに文字範囲の配列を生成します。
	 * @param cs
	 * @return
	 */
	private static CharRange[] makeRanges(char[] cs) {
		// 文字集合が空であれば文字範囲の配列も空となる
		if (cs.length == 0) return new CharRange[0];
		
		// 重複した要素を排除した配列を生成する
		final char[] uniq = distinct(cs);
		
		// 重複排除後の配列の長さが1なら、文字範囲の配列の長さも1
		// この時点で結果を返す
		if (uniq.length == 1) return new CharRange[] { new CharRange((char)uniq[0], (char)uniq[0]) };
		
        // 文字の範囲を構成するメンバを一時的に格納するバッファ
		final Queue<CharRange> rangesBuff = new LinkedList<>();
        // 文字の範囲を表すオブジェクトを一時的に格納するバッファ
		final HollowedCharQueue charsBuff = new HollowedCharQueue();
		
		// 1文字ずつ処理を行う
		for (int i = 0; i < uniq.length; i ++) {
			// 今回処理対象の文字
			final char curr = uniq[i];
			// 前回処理対象の文字（ただし初回のみ今回処理対象の文字自身）
			final char prev = i == 0 ? uniq[0] : uniq[i - 1];
			// 前回文字から今回文字までの間のコードポイントの増分を計算
			final int increment = curr - prev;
			
            // コード増分が1より大きい＝文字の範囲の分断が生じる
			if (increment > 1) {
                // 範囲メンバを格納しているバッファの内容をもとに範囲オブジェクトを生成
                // 範囲オブジェクト用のバッファに追加
				rangesBuff.add(new CharRange(charsBuff.peekFirst(), charsBuff.peekLast()));
                // 範囲メンバを格納しているバッファはクリア
				charsBuff.clear();
			}
            // 文字を文字範囲メンバ用のバッファに追加
			charsBuff.push(curr);
		}
		
		if (charsBuff.size() > 0) {
			rangesBuff.add(new CharRange(charsBuff.peekFirst(), charsBuff.peekLast()));
		}
		
		return rangesBuff.toArray(new CharRange[0]);
	}
	/**
	 * 文字範囲の最適化のためマージを試みます。
	 * @param rs
	 * @return
	 */
	private static CharRange[] tryMerge(CharRange[] rs) {
		// 文字範囲の数が0か1の場合は最適化の余地がないため即座に処理を終える
		if (rs.length <= 1) return rs;
		// 文字範囲をその開始文字の自然順序でソート
		// その後先頭の要素から順番にスタックに格納しつつ最適化を試みる
		return Arrays.stream(rs).sorted(CharRangeCharClass::compare2Ranges)
				.reduce(new Stack<CharRange>(), CharRangeCharClass::tryMerge_accumulator, 
						CharRangeCharClass::tryMerge_combinator).toArray(new CharRange[0]);
	}
	/**
	 * 2つの文字範囲をその開始文字の自然順序で比較します。
	 * @param left
	 * @param right
	 * @return
	 */
	private static int compare2Ranges(CharRange left, CharRange right) {
		return left.start - right.start;
	}
	/**
	 * 2つの文字範囲のスタックを合成して1つのスタックにします。
	 * @param a
	 * @param b
	 * @return
	 */
	private static Stack<CharRange> tryMerge_combinator(Stack<CharRange> a, Stack<CharRange> b) {
		a.addAll(b);
		return a;
	}
	/**
	 * 処理済み文字範囲のスタックと新しい処理対象の文字範囲をとり、
	 * 直近処理済み文字範囲の新しい処理対象の文字範囲のマージを試みます。
	 * @param stack 処理済み文字範囲のスタック
	 * @param range 新しい処理対象の文字範囲
	 * @return
	 */
	private static Stack<CharRange> tryMerge_accumulator(Stack<CharRange> stack, CharRange range) {
		// スタックが空（＝集約ループの初回）かどうかチェック
        if (stack.isEmpty()) {
        	// 空の場合は合成の試行はせず単に今回処理対象の文字範囲をスタックに追加
            stack.push(range);
            // 次回処理のためスタックを呼び出し元に返す
            return stack;
        }
        // 直近処理済み文字範囲と今回処理対象の文字範囲の合成を試みる
        final Optional<CharRange> res = tryMerge_accumulator_merge2Ranges(stack.peek(), range);
        // 合成が成功したかどうかチェック
        if (res.isPresent()) {
        	// 成功した場合は直近処理済みの文字範囲をスタックから除去
            stack.pop();
            // 合成済みの文字範囲をスタックに追加
            stack.push(res.get());
        } else {
        	// 失敗した場合は単に今回処理対象の文字範囲をスタックに追加
            stack.push(range);
        }
        // 次回処理のためスタックを呼び出し元に返す
        return stack;
	}
	/**
	 * 2つの文字範囲の合成を試行します。
	 * @param left
	 * @param right
	 * @return
	 */
	private static Optional<CharRange> tryMerge_accumulator_merge2Ranges(CharRange left, CharRange right) {
		// ［条件1］左辺.開始 <= 右辺.開始　かつ　［条件2］右辺.開始 <= 左辺.終了
		// ※条件1は先行して実施されるソート処理によりその成立が約束されているため再チェックはしない。
		if (/* left.start <= right.start && */ right.start <= left.end) {
			
			// ［条件3］左辺.終了 < 右辺.終了　
			if (left.end < right.end) {
				// 左辺と右辺に重なる部分があり、ただし右辺は左辺に完全には包含されない場合
				// 合成成功：　左辺.開始 から 右辺.終了 までの新しい範囲を作成して呼び出し元に返す
                return Optional.of(new CharRange(left.start, right.end));
            }
			
			// 右辺は左辺に完全に包含されている場合
			// 合成成功：　呼び出し元には左辺をそのまま返す
            return Optional.of(left);
		}
		// 左辺と右辺の文字範囲に重なる部分はない
		// 合成失敗：　呼び出し元には値を返さない
		return Optional.empty();
	}
}