package org.unclazz.parsec.sample.csv;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.unclazz.parsec.util.ReadOnlyList;

public final class Row implements Iterable<Cell> {
	private static final Row _empty = new Row(new Cell[0]);
	public static Row of(Cell...cells) {
		if (cells == null) throw new NullPointerException("\"cells\" must not be null.");
		if (cells.length == 0) return _empty;
		return new Row(cells);
	}
	public static Row of(Collection<Cell> cells) {
		if (cells == null) throw new NullPointerException("\"cells\" must not be null.");
		if (cells.isEmpty()) return _empty;
		return new Row(cells.toArray(new Cell[0]));
	}
	
	private final Cell[] _cells;
	private String _toStringCache;
	private List<Cell> _cellsCache;
	private Row(Cell[] cells) {
		_cells = cells;
	}
	public Cell get(int i) {
		if (i < 0 || _cells.length <= i) throw new IndexOutOfBoundsException("index \"i\" is out of bounds.");
		return _cells[i];
	}
	public Cell get(int i, Cell orElse) {
		if (i < 0 || _cells.length <= i) return orElse;
		return _cells[i];
	}
	public Cell get(int i, String orElse) {
		if (i < 0 || _cells.length <= i) return Cell.of(orElse);
		return _cells[i];
	}
	public int size() {
		return _cells.length;
	}
	public List<Cell> cells() {
		if (_cellsCache == null) _cellsCache = ReadOnlyList.of(_cells);
		return _cellsCache;
	}
	@Override
	public String toString() {
		if (_toStringCache == null) {
			final StringBuilder buf = new StringBuilder();
			for (int i = 0; i < _cells.length; i ++) {
				if (i > 0) buf.append(',');
				buf.append(_cells[i]);
			}
			return buf.toString();
		}
		return _toStringCache;
	}
	@Override
	public Iterator<Cell> iterator() {
		return cells().iterator();
	}
}
