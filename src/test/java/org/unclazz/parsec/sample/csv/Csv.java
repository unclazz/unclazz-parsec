package org.unclazz.parsec.sample.csv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.unclazz.parsec.TextReader;
import org.unclazz.parsec.ValResult;
import org.unclazz.parsec.util.ReadOnlyList;

public final class Csv implements Iterable<Row> {
	private static final CsvParser _parser = new CsvParser();
	public static CsvBuilder builder() {
		return new CsvBuilder();
	}
	public static Csv parse(String text) {
		final ValResult<Csv> result = _parser.parse(text);
		if (result.isSuccessful()) return result.value();
		else throw new RuntimeException(result.message());
	}
	public static Csv parse(File file, Charset charset) throws IOException {
		final ValResult<Csv> result = _parser.parse(TextReader.from(file, charset));
		if (result.isSuccessful()) return result.value();
		else throw new RuntimeException(result.message());
	}
	public static Csv parse(InputStream stream, Charset charset) throws IOException {
		final ValResult<Csv> result = _parser.parse(TextReader.from(stream, charset));
		if (result.isSuccessful()) return result.value();
		else throw new RuntimeException(result.message());
	}
	public static Csv parse(Reader reader) throws IOException {
		final ValResult<Csv> result = _parser.parse(TextReader.from(reader));
		if (result.isSuccessful()) return result.value();
		else throw new RuntimeException(result.message());
	}
	
	private final Row[] _rows;
	private List<Row> _rowsCache;
	Csv(Row[] rows) {
		if (rows == null) throw new NullPointerException("\"rows\" must not be null.");
		_rows = rows;
	}
	public List<Row> rows() {
		if (_rowsCache == null) _rowsCache = ReadOnlyList.of(_rows);
		return _rowsCache;
	}
	public int size() {
		return _rows.length;
	}
	public Row get(int i) {
		if (i < 0 || _rows.length <= i) throw new IndexOutOfBoundsException("index \"i\" is out of bounds.");
		return _rows[i];
	}
	@Override
	public Iterator<Row> iterator() {
		return rows().iterator();
	}
}
