package org.unclazz.parsec.sample.csv;

import java.util.LinkedList;
import java.util.List;

public final class CsvBuilder {
	private final List<Row> _rows = new LinkedList<>();
	
	CsvBuilder() {}
	
	public CsvBuilder append(String...cells) {
		final List<Cell> buf = new LinkedList<>();
		for (final String cell : cells) {
			buf.add(Cell.of(cell));
		}
		_rows.add(Row.of(buf.toArray(new Cell[buf.size()])));
		return this;
	}
	public CsvBuilder append(Cell...cells) {
		_rows.add(Row.of(cells));
		return this;
	}
	public CsvBuilder append(Row row) {
		if (row == null) throw new NullPointerException();
		_rows.add(row);
		return this;
	}
	public Csv build() {
		return new Csv(_rows.toArray(new Row[_rows.size()]));
	}
}
