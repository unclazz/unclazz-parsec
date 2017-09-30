package org.unclazz.parsec.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.unclazz.parsec.Context;
import org.unclazz.parsec.Mappers;
import org.unclazz.parsec.ValParser;
import org.unclazz.parsec.ValResult;
import org.unclazz.parsec.ValResultCore;
import org.unclazz.parsec.util.Tuple2;

public final class MathParserTest {

	@Test
	public void testParse() {
		final ValParser<Integer> math = new MathParser();
		
		final ValResult<Integer> result_2_add_3 = math.parse("2+3");
		final ValResult<Integer> result_2_mul_3 = math.parse("2*3");
		final ValResult<Integer> result_2_mul_3_add_1 = math.parse("2*3+1");
		final ValResult<Integer> result_2_mul_ps_3_add_1_pe = math.parse("2*(3+1)");

		result_2_add_3.ifSuccessful(a -> {
			assertThat(a, is(5));
		}, org.junit.Assert::fail);
		
		result_2_mul_3.ifSuccessful(a -> {
			assertThat(a, is(6));
		}, org.junit.Assert::fail);

		result_2_mul_3_add_1.ifSuccessful(a -> {
			assertThat(a, is(7));
		}, org.junit.Assert::fail);
		
		result_2_mul_ps_3_add_1_pe.ifSuccessful(a -> {
			assertThat(a, is(8));
		}, org.junit.Assert::fail);
	}

}

final class MathParser extends ValParser<Integer>{
	private final ValParser<Integer> number = charBetween('0', '9').repMin(1).map(Mappers::digits);
	
	private ValParser<Integer> parens() {
		return exact('(').cut().then(this::addSub).then(exact(')'));
	}
	private ValParser<Integer> factor() {
		return number.or(this::parens);
	}
	private ValParser<Integer> mulDiv() {
		return factor().then((charIn("*/").val().then(this::factor)).rep()).map(this::mulDivAddSub_mapper);
	}
	private ValParser<Integer> addSub() {
		return mulDiv().then((charIn("+-").val().then(this::mulDiv)).rep()).map(this::mulDivAddSub_mapper);
	}
	private ValParser<Integer> expr() {
		return addSub().then(eof());
	}
	@Override
	protected ValResultCore<Integer> doParse(Context ctx) throws IOException {
		ctx = ctx.configure(a -> a.setLogAppender(System.out::println));
		return expr().parse(ctx);
	}
	private Integer mulDivAddSub_mapper(Tuple2<Integer, List<Tuple2<String,Integer>>> expr) {
		if (expr.item2().isEmpty()) return expr.item1();
		int tmp = expr.item1();
		for (final Tuple2<String,Integer> t : expr.item2()) {
			if (t.item1().equals("*")) {
				tmp *= t.item2();
			} else if (t.item1().equals("/")) {
				tmp /= t.item2();
			} else if (t.item1().equals("+")) {
				tmp += t.item2();
			} else if (t.item1().equals("-")) {
				tmp -= t.item2();
			} else {
				throw new IllegalArgumentException("unknown operator.");
			}
		}
		return tmp;
	}
}