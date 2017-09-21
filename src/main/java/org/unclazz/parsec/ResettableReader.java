package org.unclazz.parsec;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

class ResettableReader extends PrependableReader {
	
	private boolean _marked;
	private final CharStack _backup = new CharStack(100);
	private final Stack<CharPosition> _marks = new Stack<>();
	
	public ResettableReader(Reader reader) {
		super(reader);
	}
	
	public void mark() {
		_marked = true;
		_marks.push(position());
	}
	public void unmark() {
		if (_marked) {
			_marks.pop();
			if (_marks.empty()) {
				_marked = false;
				_backup.clear();
			}
		}
	}
	public String capture(boolean unmark) {
		if (_marked) {
			final int delta = position().index() - _marks.peek().index();
			final int skip = _backup.size() - delta;
			final String tmp = _backup.toString(skip);
			if (unmark) unmark();
			return tmp;
		}
		return null;
	}
	public String capture() {
		return capture(false);
	}
	@Override
	public int read() throws IOException {
		final int ch = super.read();
		if (_marked && ch != -1) _backup.push((char) ch);
		return ch;
	}
	public void reset() {
		reset(false);
	}
	public void reset(boolean unmark) {
		if (_marked) {
			final CharPosition lastMark = _marks.peek();
			final int delta = position().index() - lastMark.index();
			
            // 現在の文字位置とマークした文字位置が同値ならリセットは不要
			if (delta == 0) return;
			
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