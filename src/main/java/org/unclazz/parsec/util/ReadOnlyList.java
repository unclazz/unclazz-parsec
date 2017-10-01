package org.unclazz.parsec.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * イミュータブルなリストです。
 * <p>{@link List}インターフェースで宣言されているメソッドのうち、
 * リストのコンテンツを変更するものを呼び出すと{@link UnsupportedOperationException}がスローされます。
 * リストそのものを変更する代わりに{@link ReadOnlyList#prepend(Object)}や
 * {@link ReadOnlyList#append(Object)}メソッドにより要素を追加した新しいリストを生成することができます。</p>
 */
public final class ReadOnlyList<T> implements Iterable<T>, List<T> {
	private static final ReadOnlyList<?> _empty = new ReadOnlyList<Void>(new Void[0]);
	private static void mustNotBeNull(String name, Object target) {
		if (target == null) throw new NullPointerException
		(String.format("argument \"%s\" must not be null.", name));
	}
	/**
	 * 指定された要素を内容とするリストを返します。
	 * @param items リストの要素
	 * @return リスト
	 * @param <T> リストの要素型
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static<T> ReadOnlyList<T> of(T...items){
		mustNotBeNull("items", items);
		if (items.length == 0) return (ReadOnlyList<T>)_empty;
		else return new ReadOnlyList<T>(Arrays.copyOf(items, items.length));
	}
	/**
	 * 指定された要素を内容とするリストを返します。
	 * @param items リストの要素
	 * @return リスト
	 * @param <T> リストの要素型
	 */
	@SuppressWarnings("unchecked")
	public static<T> ReadOnlyList<T> of(Collection<? extends T> items){
		mustNotBeNull("items", items);
		return new ReadOnlyList<T>((T[])items.toArray());
	}
	
	private ReadOnlyList(T[] items) {
		_items = items;
	}
	
	private final T[] _items;
	
	@Override
	public int size() {
		return _items.length;
	}
	@Override
	public T get(int i) {
		if (i < 0 || _items.length <= i) throw new IndexOutOfBoundsException();
		return _items[i];
	}
	/**
	 * 末尾に要素を加えた新しいリストを返します。
	 * @param item 新しい要素
	 * @return 新しいリスト
	 */
	public ReadOnlyList<T> append(T item){
		if (size() == 0) return of(item);
		final T[] newItems = Arrays.copyOf(_items, _items.length + 1);
		newItems[_items.length] = item;
		return new ReadOnlyList<>(newItems);
	}
	/**
	 * 先頭に要素を加えた新しいリストを返します。
	 * @param item 新しい要素
	 * @return 新しいリスト
	 */
	public ReadOnlyList<T> prepend(T item){
		if (size() == 0) return of(item);
		@SuppressWarnings("unchecked")
		final T[] newItems = (T[]) new Object[_items.length + 1];
		System.arraycopy(_items, 0, newItems, 1, _items.length);
		newItems[0] = item;
		return new ReadOnlyList<>(newItems);
	}
	/**
	 * 末尾に要素を加えた新しいリストを返します。
	 * @param items 新しい要素
	 * @return 新しいリスト
	 */
	public ReadOnlyList<T> appendAll(@SuppressWarnings("unchecked") T...items){
		if (size() == 0) return new ReadOnlyList<>(items);
		if (items.length == 0) return this;
		final T[] newItems = Arrays.copyOf(_items, _items.length + items.length);
		for (int i = 0; i < items.length; i++) {
			newItems[_items.length + i] = items[i];
		}
		return new ReadOnlyList<>(newItems);
	}
	/**
	 * 末尾に要素を加えた新しいリストを返します。
	 * @param items 新しい要素
	 * @return 新しいリスト
	 */
	@SuppressWarnings("unchecked")
	public ReadOnlyList<T> appendAll(Collection<? extends T> items){
		return appendAll((T[])items.toArray());
	}
	/**
	 * 先頭に要素を加えた新しいリストを返します。
	 * @param items 新しい要素
	 * @return 新しいリスト
	 */
	public ReadOnlyList<T> prependAll(@SuppressWarnings("unchecked") T...items){
		if (size() == 0) return new ReadOnlyList<>(items);
		if (items.length == 0) return this;
		final T[] newItems = Arrays.copyOf(items, _items.length + items.length);
		for (int i = 0; i < _items.length; i++) {
			newItems[items.length + i] = _items[i];
		}
		return new ReadOnlyList<>(newItems);
	}
	/**
	 * 先頭に要素を加えた新しいリストを返します。
	 * @param items 新しい要素
	 * @return 新しいリスト
	 */
	@SuppressWarnings("unchecked")
	public ReadOnlyList<T> prependAll(Collection<? extends T> items){
		return prependAll((T[])items.toArray());
	}
	@Override
	public Iterator<T> iterator() {
		return new ReadOnlyListIterator<>(_items);
	}
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder().append('[');
		for (int i = 0; i < _items.length; i++) {
			if (i > 0) buf.append(", ");
			buf.append(_items[i]);
		}
		return buf.append(']').toString();
	}
	
	private RuntimeException listIsReadOnly() {
		return  new UnsupportedOperationException("this list is read-only.");
	}

	/* ---------- 以下、List<T>インターフェースを実装するためのメンバー ---------- */
	@Override
	public boolean add(T e) {
		throw listIsReadOnly();
	}
	@Override
	public void add(int index, T element) {
		throw listIsReadOnly();
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw listIsReadOnly();
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw listIsReadOnly();
	}
	@Override
	public void clear() {
		throw listIsReadOnly();
	}
	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < _items.length; i ++) {
			final T item = _items[i];
			if (item != null && item.equals(o)) return true;
		}
		return false;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		for (final Object o : c) {
			if (!contains(o)) return false;
		}
		return true;
	}
	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < _items.length; i ++) {
			final T item = _items[i];
			if ((item != null && item.equals(o)) || (item == null && o == null)) return i;
		}
		return -1;
	}
	@Override
	public boolean isEmpty() {
		return _items.length == 0;
	}
	@Override
	public int lastIndexOf(Object o) {
		for (int i = _items.length - 1; 0 <= i; i --) {
			final T item = _items[i];
			if ((item != null && item.equals(o)) || (item == null && o == null)) return i;
		}
		return -1;
	}
	@Override
	public ListIterator<T> listIterator() {
		return new ReadOnlyListIterator<>(_items);
	}
	@Override
	public ListIterator<T> listIterator(int index) {
		return new ReadOnlyListIterator<>(_items, index);
	}
	@Override
	public boolean remove(Object o) {
		throw listIsReadOnly();
	}
	@Override
	public T remove(int index) {
		throw listIsReadOnly();
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		throw listIsReadOnly();
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		throw listIsReadOnly();
	}
	@Override
	public T set(int index, T element) {
		throw listIsReadOnly();
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		if ((fromIndex < 0 || toIndex > size() || fromIndex > toIndex)) throw new IndexOutOfBoundsException();
		return new ReadOnlyList<>(Arrays.copyOfRange(_items, fromIndex, toIndex));
	}
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(_items, _items.length);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <U> U[] toArray(U[] a) {
		if (a.length < _items.length) return (U[]) Arrays.copyOf(_items, _items.length); 
		System.arraycopy(_items, 0, a, 0, _items.length);
		return a;
	}
	/* ---------- 以上、List<T>インターフェースを実装するためのメンバー ---------- */
}
