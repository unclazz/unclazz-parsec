package org.unclazz.parsec.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ReadOnlyListTest {

	@Test
	public final void testOfTArray() {
		try {
			final String[] a = null;
			ReadOnlyList.of(a);
			fail();
		} catch (NullPointerException e) {
			// OK.
		}
	}

	@Test
	public final void testOfCollectionOfQextendsT() {
		try {
			final List<String> a = null;
			ReadOnlyList.of(a);
			fail();
		} catch (NullPointerException e) {
			// OK.
		}
	}

	@Test
	public final void testSize() {
		assertThat(ReadOnlyList.of(1, 2, 3).size(), is(3));
		assertThat(ReadOnlyList.of().size(), is(0));
	}

	@Test
	public final void testGet() {
		assertThat(ReadOnlyList.of(1, 2, 3).get(0), is(1));
		assertThat(ReadOnlyList.of(1, 2, 3).get(2), is(3));
		try {
			ReadOnlyList.of(1, 2, 3).get(3);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// OK.
		}
	}

	@Test
	public final void testAppend() {
		assertThat(ReadOnlyList.of(0, 1, 2).append(3).get(3), is(3));
		assertThat(ReadOnlyList.of().append(3).get(0), is(3));
	}

	@Test
	public final void testPrepend() {
		assertThat(ReadOnlyList.of(0, 1, 2).prepend(3).get(0), is(3));
		assertThat(ReadOnlyList.of().prepend(3).get(0), is(3));
	}

	@Test
	public final void testAppendAllTArray() {
		assertThat(ReadOnlyList.of(0, 1, 2).appendAll(3, 4).get(4), is(4));
		assertThat(ReadOnlyList.of().appendAll(3, 4).get(1), is(4));
	}

	@Test
	public final void testAppendAllCollectionOfQextendsT() {
		assertThat(ReadOnlyList.of(0, 1, 2).appendAll(Arrays.asList(3, 4)).get(4), is(4));
		assertThat(ReadOnlyList.of().appendAll(Arrays.asList(3, 4)).get(1), is(4));
	}

	@Test
	public final void testPrependAllTArray() {
		assertThat(ReadOnlyList.of(0, 1, 2).prependAll(3, 4).get(0), is(3));
		assertThat(ReadOnlyList.of().prependAll(3, 4).get(0), is(3));
	}

	@Test
	public final void testPrependAllCollectionOfQextendsT() {
		assertThat(ReadOnlyList.of(0, 1, 2).prependAll(Arrays.asList(3, 4)).get(0), is(3));
		assertThat(ReadOnlyList.of().prependAll(Arrays.asList(3, 4)).get(0), is(3));
	}

	@Test
	public final void testIterator() {
		assertThat(ReadOnlyList.of(1, 2, 3).iterator().next(), is(1));
		try {
			ReadOnlyList.of().iterator().next();
			fail();
		} catch (NoSuchElementException e) {
			// OK.
		}
	}

	@Test
	public final void testStream() {
		assertThat(ReadOnlyList.of(1, 2, 3).stream().reduce((a, b) -> a + b).get(), is(6));
	}

	@Test
	public final void testToString() {
		assertThat(ReadOnlyList.of().toString(), is("[]"));
		assertThat(ReadOnlyList.of(1).toString(), is("[1]"));
		assertThat(ReadOnlyList.of(1, 2, 3).toString(), is("[1, 2, 3]"));
	}

	@Test
	public final void testAddT() {
		try {
			ReadOnlyList.of(0, 1, 2).add(3);
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testAddIntT() {
		try {
			ReadOnlyList.of(0, 1, 2).add(1, 3);
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testAddAllCollectionOfQextendsT() {
		try {
			ReadOnlyList.of(0, 1, 2).addAll(new ArrayList<>());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testAddAllIntCollectionOfQextendsT() {
		try {
			ReadOnlyList.of(0, 1, 2).addAll(1, new ArrayList<>());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testClear() {
		try {
			ReadOnlyList.of(0, 1, 2).clear();
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testContains() {
		assertTrue(ReadOnlyList.of(0,1,2).contains(0));
		assertTrue(ReadOnlyList.of(0,1,2).contains(2));
		assertFalse(ReadOnlyList.of(0,1,2).contains(3));
	}

	@Test
	public final void testContainsAll() {
		assertTrue(ReadOnlyList.of(0,1,2).containsAll(Arrays.asList(0, 1)));
		assertTrue(ReadOnlyList.of(0,1,2).containsAll(Arrays.asList()));
		assertFalse(ReadOnlyList.of(0,1,2).containsAll(Arrays.asList(0, 1, 3)));
	}

	@Test
	public final void testIndexOf() {
		assertThat(ReadOnlyList.of().indexOf(0), is(-1));
		assertThat(ReadOnlyList.of(0, 1, 2).indexOf(3), is(-1));
		assertThat(ReadOnlyList.of(0, 1, 2).indexOf(0), is(0));
		assertThat(ReadOnlyList.of(0, 1, 2).indexOf(2), is(2));
		assertThat(ReadOnlyList.of(0, 1, 2, 1).indexOf(1), is(1));
	}

	@Test
	public final void testIsEmpty() {
		assertThat(ReadOnlyList.of().isEmpty(), is(true));
		assertThat(ReadOnlyList.of(1).isEmpty(), is(false));
		assertThat(ReadOnlyList.of(1, 2, 3).isEmpty(), is(false));
	}

	@Test
	public final void testLastIndexOf() {
		assertThat(ReadOnlyList.of().lastIndexOf(0), is(-1));
		assertThat(ReadOnlyList.of(0, 1, 2).lastIndexOf(3), is(-1));
		assertThat(ReadOnlyList.of(0, 1, 2).lastIndexOf(0), is(0));
		assertThat(ReadOnlyList.of(0, 1, 2).lastIndexOf(2), is(2));
		assertThat(ReadOnlyList.of(0, 1, 2, 1).lastIndexOf(1), is(3));
	}

	@Test
	public final void testListIterator() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator().nextIndex(), is(0));
	}

	@Test
	public final void testListIteratorInt() {
		assertThat(ReadOnlyList.of(0, 1, 2).listIterator(1).nextIndex(), is(1));
	}

	@Test
	public final void testRemoveObject() {
		try {
			ReadOnlyList.of(0, 1, 2).remove(3);
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testRemoveInt() {
		try {
			ReadOnlyList.of(0, 1, 2).remove(Integer.valueOf(2));
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testRemoveAll() {
		try {
			ReadOnlyList.of(0, 1, 2).removeAll(new ArrayList<>());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testRetainAll() {
		try {
			ReadOnlyList.of(0, 1, 2).retainAll(new ArrayList<>());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testSet() {
		try {
			ReadOnlyList.of(0, 1, 2).set(3, 2);
			fail();
		} catch (UnsupportedOperationException e) {
			// OK.
		}
	}

	@Test
	public final void testSubList() {
		assertTrue(ReadOnlyList.of().subList(0, 0).toArray().length == 0);
		assertTrue(ReadOnlyList.of(0, 1, 2).subList(0, 0).toArray().length == 0);
		assertTrue(ReadOnlyList.of(0, 1, 2).subList(0, 1).toArray().length == 1);
		assertTrue(ReadOnlyList.of(0, 1, 2).subList(0, 2).toArray().length == 2);
		assertTrue(ReadOnlyList.of(0, 1, 2).subList(0, 3).toArray().length == 3);
		
		try {
			ReadOnlyList.of().subList(0, 1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// OK
		}
		try {
			ReadOnlyList.of().subList(1, 1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// OK
		}
		try {
			ReadOnlyList.of().subList(1, 0);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// OK
		}
	}

	@Test
	public final void testToArray() {
		assertTrue(ReadOnlyList.of().toArray().length == 0);
		assertTrue(ReadOnlyList.of(0, 1, 2).toArray().length == 3);
	}

	@Test
	public final void testToArrayUArray() {
		assertTrue(ReadOnlyList.of().toArray(new Integer[0]).length == 0);
		assertTrue(ReadOnlyList.of().toArray(new Integer[2]).length == 2);
		assertTrue(ReadOnlyList.of(0, 1, 2).toArray(new Integer[0]).length == 3);
		assertTrue(ReadOnlyList.of(0, 1, 2).toArray(new Integer[4]).length == 4);
	}

}
