package org.unclazz.parsec;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

/**
 * リセット機能を持つリーダーです。
 */
class ResettableReader extends PrependableReader {
	
	private boolean _marked;
	private final CharStack _backup = new CharStack(100);
	private final Stack<CharPosition> _marks = new Stack<>();
	
	ResettableReader(Reader reader) {
		super(reader);
	}
	
	@Override
	public final boolean markSupported() {
		return true;
	}
	@Override
	public final void mark(int readAheadLimit) {
		mark();
	}
	/**
	 * 現在の文字位置にマークを設定します。
	 */
	public final void mark() {
		_marked = true;
		_marks.push(position());
	}
	/**
	 * 直近のマークを解除します。
	 */
	public final void unmark() {
		if (_marked) {
			_marks.pop();
			if (_marks.empty()) {
				_marked = false;
				_backup.clear();
			}
		}
	}
	/**
	 * 直近マークした文字位置から現在の文字位置の1つ前までの文字列をキャプチャします。
	 * @param unmark マーク解除も同時に行う場合{@code true}
	 * @return
	 */
	public final String capture(boolean unmark) {
		if (_marked) {
			final int delta = position().index() - _marks.peek().index();
			final int skip = _backup.size() - delta;
			final String tmp = _backup.toString(skip);
			if (unmark) unmark();
			return tmp;
		}
		return null;
	}
	/**
	 * 直近マークした文字位置から現在の文字位置の1つ前までの文字列をキャプチャします。
	 * @return マーク解除も同時に行う場合{@code true}
	 */
	public final String capture() {
		return capture(false);
	}
	@Override
	public int readOne() throws IOException {
		final int ch = super.readOne();
		if (_marked && ch != -1) _backup.push((char) ch);
		return ch;
	}
	/**
	 * 直近マークした位置まで文字位置を戻します。
	 */
	@Override
	public final void reset() {
		reset(false);
	}
	/**
	 * 直近マークした位置まで文字位置を戻します。
	 * @param unmark
	 */
	public final void reset(boolean unmark) {
		if (_marked) {
			final CharPosition lastMark = _marks.peek();
			final int delta = position().index() - lastMark.index();
			
            // 現在の文字位置とマークした文字位置が同値ならリセットは不要
			if (delta == 0) {
				if (unmark) unmark();
				return;
			}
			
            // それ以外の場合は完全もしくは部分リセットが必要
            // バックアップの現状の情報を一時変数に移動
			final int bkSize = _backup.size();
			
            // マークした文字位置との間の添字差分とバックアップされていた要素数が一致するかどうかチェック
			if (delta == bkSize) {
                // 一致する場合は完全リセット
                // バックアップされていた要素すべてを使用してリセットを行う
				reattach(lastMark, _backup.popAll());
			} else if (delta < bkSize) {
                // 一致しない場合は部分リセット
                // バックアップされていた要素のうち必要な分だけを使用してリセットを行う
				reattach(lastMark, _backup.pop(delta));
			} else {
				throw new IllegalStateException();
			}
			if (unmark) unmark();
		}
	}
}