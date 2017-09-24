package org.unclazz.parsec.data;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

final class ReadOnlyListIterator<T> implements Iterator<T>, ListIterator<T>{
	private int _index;
	private final T[] _items;
	
	ReadOnlyListIterator(T[] items){
		if (items == null) throw new NullPointerException();
		_items = items;
	}
	ReadOnlyListIterator(T[] items, int index){
		if (items == null) throw new NullPointerException();
		if (index < 0 || index > items.length) throw new IndexOutOfBoundsException();
		_items = items;
		_index = index;
	}
	
	@Override
	public void add(T e) { 
		throw listIsReadOnly(); 
	}
	@Override
	public boolean hasNext() { 
		return _index < _items.length; 
	}
	@Override
	public boolean hasPrevious() {
		return 0 < _index;
	}
	@Override
	public T next() {
		if (hasNext()) return _items[_index ++]; 
		throw new NoSuchElementException();
	}
	@Override
	public int nextIndex() {
		return _index;
	}
	@Override
	public T previous() {
		if (_index <= 0) throw new NoSuchElementException();
		return _items[-- _index];
	}
	@Override
	public int previousIndex() {
		return _index - 1;
	}
	@Override
	public void remove() { 
		throw  listIsReadOnly(); 
	}
	@Override
	public void set(T e) { 
		throw  listIsReadOnly(); 
	}
	private RuntimeException listIsReadOnly() {
		return  new UnsupportedOperationException("this list is read-only.");
	}
}