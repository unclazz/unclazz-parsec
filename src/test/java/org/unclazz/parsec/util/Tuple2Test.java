package org.unclazz.parsec.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.unclazz.parsec.util.Tuple;
import org.unclazz.parsec.util.Tuple2;

public class Tuple2Test {

	@Test
	public void testOf() {
		final Tuple2<Integer,String> p = Tuple.of(100, "200");
		assertThat(p.size(), is(2));
		assertThat(p.item1(), is(100));
		assertThat(p.item2(), is("200"));
	}

	@Test
	public void testItem1() {
		assertThat(Tuple.of(100, 200).item1(), is(100));
	}

	@Test
	public void testItem2() {
		assertThat(Tuple.of(100, 200).item2(), is(200));
	}

	@Test
	public void testSize() {
		assertThat(Tuple.of(100, 200).size(), is(2));
	}

	@Test
	public void testValues() {
		assertThat(Tuple.of(100, 200).values(), is(new Object[] { 100, 200 }));
	}

}
