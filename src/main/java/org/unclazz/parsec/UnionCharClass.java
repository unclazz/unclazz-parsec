package org.unclazz.parsec;

import java.util.Arrays;

/**
 * 2つの文字クラスを合成する文字クラスです。
 */
final class UnionCharClass extends CharClass{
	private final CharClass[] _items;
	
	UnionCharClass(CharClass left, CharClass right) {
		ParsecUtility.mustNotBeNull("left", left);
		ParsecUtility.mustNotBeNull("right", right);
		
		// 文字クラスを配列化する
		// 左辺もしくは右辺がUnionCharClassである場合は最適化を行う
		_items = makeArray(left, right);
	}
	
	@Override
	public boolean contains(int ch) {
		for (int i = 0; i < _items.length; i ++) {
			if (_items[i].contains(ch)) return true;
		}
		return false;
	}
	private CharClass[] makeArray(CharClass left, CharClass right) {
		// 左辺が自身と同じクラスかどうかチェック
		if (left instanceof UnionCharClass) {
			// 同じ場合
			// キャストを行ってプライベートメンバーを露出させる
			final UnionCharClass ucLeft = (UnionCharClass) left;
			// 右辺が自身と同じクラスかどうかチェック
			if (right instanceof UnionCharClass) {
				// 同じ場合
				// キャストを行ってプライベートメンバーを露出させる
				final UnionCharClass ucRight = (UnionCharClass) right;
				
				// 左辺をベースに左辺と右辺双方を含む新しい配列を生成する
				final CharClass[] newItems = Arrays.copyOf(ucLeft._items,
						ucLeft._items.length + ucRight._items.length);
				System.arraycopy(ucRight._items, 0, newItems, ucLeft._items.length, ucRight._items.length);
				return newItems;
			} else {
				// 異なる場合
				// 左辺をベースに左辺のメンバーと右辺のクラスを含む新しい配列を生成する
				final CharClass[] newItems = Arrays.copyOf(ucLeft._items, ucLeft._items.length + 1);
				newItems[ucLeft._items.length] = right;
				return newItems;
			}
		} else {
			// 異なる場合
			// 右辺が自身と同じクラスかどうかチェック
			if (right instanceof UnionCharClass) {
				// 同じ場合
				// キャストを行ってプライベートメンバーを露出させる
				final UnionCharClass ucRight = (UnionCharClass) right;
				// 右辺をベースに右辺のメンバーと左辺のクラスを含む新しい配列を生成する
				final CharClass[] newItems = Arrays.copyOf(ucRight._items, ucRight._items.length + 1);
				newItems[ucRight._items.length] = left;
				return newItems;
			} else {
				// 異なる場合
				// 左辺と右辺、それぞれのクラスからなる配列を生成する
				return new CharClass[] {left, right};
			}
		}
	}
}