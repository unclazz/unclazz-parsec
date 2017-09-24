package org.unclazz.parsec.data;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ReadOnlyListIteratorTest {

	@Test
	public final void testAdd() {
		try {
			ReadOnlyList.of("hello").listIterator().add("bonjour");
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testHasNext() {
		assertTrue(ReadOnlyList.of(0, 1, 2).listIterator().hasNext());
		assertTrue(ReadOnlyList.of(0, 1, 2).listIterator(2).hasNext());
		assertFalse(ReadOnlyList.of(0, 1, 2).listIterator(3).hasNext());
	}

	@Test
	public final void testHasPrevious() {
		assertFalse(ReadOnlyList.of(0, 1, 2).listIterator().hasPrevious());
		assertTrue(ReadOnlyList.of(0, 1, 2).listIterator(1).hasPrevious());
	}

	@Test
	public final void testNext() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator().next(), is(0));
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator(2).next(), is(2));
		try {
			ReadOnlyList.of(0, 1, 2).listIterator(3).next();
			fail();
		} catch (NoSuchElementException e) {
			// OK.
		}
	}

	@Test
	public final void testNextIndex() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator().nextIndex(), is(0));
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator(2).nextIndex(), is(2));
	}

	@Test
	public final void testPrevious() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator(1).previous(), is(0));
		try {
			ReadOnlyList.of(0, 1, 2).listIterator().previous();
			fail();
		} catch (NoSuchElementException e) {
			// OK.
		}
		try {
			ReadOnlyList.of(0, 1, 2).listIterator(0).previous();
			fail();
		} catch (NoSuchElementException e) {
			// OK.
		}
	}

	@Test
	public final void testPreviousIndex() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator().previousIndex(), is(-1));
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator(2).previousIndex(), is(1));
	}

	@Test
	public final void testRemove() {
		try {
			ReadOnlyList.of("hello").listIterator().remove();
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testSet() {
		try {
			ReadOnlyList.of("hello").listIterator().set("bonjour");
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

}
