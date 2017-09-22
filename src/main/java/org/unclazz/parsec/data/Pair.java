package org.unclazz.parsec.data;

public final class Pair<T1,T2> implements Tuple {
	public static<T1, T2> Pair<T1, T2> of(T1 item1, T2 item2){
		return new Pair<>(item1, item2);
	}
	
	private final T1 _item1;
	private final T2 _item2;
	
	private Pair(T1 item1, T2 item2) {
		_item1 = item1;
		_item2 = item2;
	}
	
	public T1 item1() {
		return _item1;
	}
	public T2 item2() {
		return _item2;
	}
	@Override
	public int size() {
		return 2;
	}
	@Override
	public Object[] values() {
		return new Object[] { _item1, _item2 };
	}
}