package org.unclazz.parsec.sample.csv;

import java.io.IOException;

import org.unclazz.parsec.Context;
import org.unclazz.parsec.Parser;
import org.unclazz.parsec.ValParser;
import org.unclazz.parsec.ValResultCore;

final class CsvParser extends ValParser<Csv> {
	private final ValParser<Csv> _csvContent;
	CsvParser() {
		final Parser quote = exact('"');
		final Parser crlf = keyword("\r\n").or(exact('\n')).or(exact('\r'));
		final Parser escape = quote.then(quote);
		final ValParser<Cell> cellValue = except('"').or(escape).repMin(1).map(a -> Cell.of(a, true));
		final ValParser<Cell> cellNoQuote = charNotIn('"', ',', '\r', '\n').repMin(1).map(a -> Cell.of(a));
		final ValParser<Cell> cell = (quote.cut().then(cellValue).then(quote)).or(cellNoQuote);
		final ValParser<Row> row = cell.repMin(1, exact(',')).map(a -> Row.of(a),true);
		final ValParser<Csv> rows = row.repMin(1, crlf).reduce(Csv::builder, (a, b) -> a.append(b), a -> a.build());
		_csvContent = rows.then(space()).then(eof());
	}
	
	@Override
	protected ValResultCore<Csv> doParse(Context ctx) throws IOException {
		return _csvContent.parse(ctx);
	}
}
