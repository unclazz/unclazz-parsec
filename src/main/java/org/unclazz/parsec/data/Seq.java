package org.unclazz.parsec.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public final class Seq<T> implements Iterable<T> {
	private static final Seq<?> _empty = new Seq<Void>(new Void[0]);
	private static void mustNotBeNull(String name, Object target) {
		if (target == null) throw new IllegalArgumentException
		(String.format("argument \"%s\" must not be null.", name));
	}
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static<T> Seq<T> of(T...items){
		mustNotBeNull("items", items);
		if (items.length == 0) return (Seq<T>)_empty;
		else return new Seq<T>(Arrays.copyOf(items, items.length));
	}
	@SuppressWarnings("unchecked")
	public static<T> Seq<T> of(Collection<T> items){
		mustNotBeNull("items", items);
		return new Seq<T>((T[])items.stream().toArray());
	}
	
	@SafeVarargs
	private Seq(T... items) {
		_items = items;
	}
	
	private final T[] _items;
	
	public int size() {
		return _items.length;
	}
	public T get(int i) {
		if (_items.length <= i) throw new IndexOutOfBoundsException();
		return _items[i];
	}
	public Seq<T> append(T item){
		if (size() == 0) return new Seq<>(item);
		final T[] newItems = Arrays.copyOf(_items, _items.length + 1);
		newItems[_items.length] = item;
		return new Seq<>(newItems);
	}
	public Seq<T> prepend(T item){
		if (size() == 0) return new Seq<>(item);
		@SuppressWarnings("unchecked")
		final T[] newItems = (T[]) new Object[_items.length + 1];
		System.arraycopy(_items, 0, newItems, 1, _items.length);
		newItems[0] = item;
		return new Seq<>(newItems);
	}
	public Seq<T> concat(T[] items){
		if (size() == 0) return new Seq<>(items);
		if (items.length == 0) return this;
		final T[] newItems = Arrays.copyOf(_items, _items.length + items.length);
		for (int i = 0; i < items.length; i++) {
			newItems[_items.length + i] = items[i];
		}
		return new Seq<>(newItems);
	}
	public Seq<T> concatLeft(T[] items){
		if (size() == 0) return new Seq<>(items);
		if (items.length == 0) return this;
		final T[] newItems = Arrays.copyOf(items, _items.length + items.length);
		for (int i = 0; i < _items.length; i++) {
			newItems[items.length + i] = _items[i];
		}
		return new Seq<>(newItems);
	}
	@Override
	public Iterator<T> iterator() {
		return new SeqIterator<>(_items);
	}
	public Stream<T> stream(){
		return Arrays.stream(_items);
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
	
	static final class SeqIterator<T> implements Iterator<T>{
		private int _index;
		private final T[] _items;
		SeqIterator(T[] items){
			_items = items;
		}
		@Override
		public boolean hasNext() {
			return _index < _items.length;
		}
		@Override
		public T next() {
			return _items[_index ++];
		}
	}
}
