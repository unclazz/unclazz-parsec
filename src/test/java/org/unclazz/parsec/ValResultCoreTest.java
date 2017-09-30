package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class ValResultCoreTest {

	@Test
	public void testOfSuccess() {
		final ValResultCore<Integer> rc0 = ValResultCore.ofSuccess(123);
		assertThat(rc0.isSuccessful(), is(true));
		assertThat(rc0.message(), is(nullValue()));
		assertThat(rc0.canBacktrack(), is(true));
		assertThat(rc0.value(), is(123));
	}

	@Test
	public void testOfFailure() {
		final ValResultCore<Integer> rc1 = ValResultCore.ofFailure("an error has occurred.");
		assertThat(rc1.isSuccessful(), is(false));
		assertThat(rc1.message(), is("an error has occurred."));
		assertThat(rc1.canBacktrack(), is(true));
		assertThat(rc1.value(), is(nullValue()));
	}

	@Test
	public void testResultCore() {
		final ValResultCore<Integer> rc0 = new ValResultCore<Integer>(true, 123, null, true);
		final ValResultCore<Integer> rc1 = new ValResultCore<Integer>(false, null, "error", true);
		final ValResultCore<Integer> rc2 = new ValResultCore<Integer>(true, 456,  null, false);
		final ValResultCore<Integer> rc3 = new ValResultCore<Integer>(false, null, "error", false);
		
		assertThat(rc0.isSuccessful(), is(true));
		assertThat(rc1.isSuccessful(), is(false));
		assertThat(rc2.isSuccessful(), is(true));
		assertThat(rc3.isSuccessful(), is(false));
		
		assertThat(rc0.message(), is(nullValue()));
		assertThat(rc1.message(), is("error"));
		assertThat(rc2.message(), is(nullValue()));
		assertThat(rc3.message(), is("error"));
		
		assertThat(rc0.canBacktrack(), is(true));
		assertThat(rc1.canBacktrack(), is(true));
		assertThat(rc2.canBacktrack(), is(false));
		assertThat(rc3.canBacktrack(), is(false));
		
		assertThat(rc0.value(), is(123));
		assertThat(rc1.value(), is(nullValue()));
		assertThat(rc2.value(), is(456));
		assertThat(rc3.value(), is(nullValue()));
	}

	@Test
	public void testAttachPosition() {
		final ValResult<Integer> r = ValResultCore.ofSuccess(123)
				.attachPosition(CharPosition.ofBof().nextColumn(), 
						CharPosition.ofBof().nextColumn().nextLine());
		
		assertThat(r.start().index(), is(1));
		assertThat(r.end().index(), is(2));
	}

	@Test
	public void testAllowBacktrack() {
		final ValResultCore<Integer> rc0 = ValResultCore.ofSuccess(123);
		assertThat(rc0.canBacktrack(), is(true));
		assertThat(rc0.allowBacktrack(true).canBacktrack(), is(true));
		assertThat(rc0.allowBacktrack(false).canBacktrack(), is(false));
		
		final ValResultCore<Integer> rc1 = ValResultCore.<Integer>ofFailure("an error has occurred.");
		assertThat(rc1.canBacktrack(), is(true));
		assertThat(rc1.allowBacktrack(true).canBacktrack(), is(true));
		assertThat(rc1.allowBacktrack(false).canBacktrack(), is(false));
	}

	@Test
	public void testIfSuccessfulRunnable() {
		final AtomicBoolean ab = new AtomicBoolean(false);
		ValResultCore.ofSuccess(123).ifSuccessful((a) -> {
			ab.set(true);
			assertThat(a, is(123));
		});
		assertThat(ab.get(), is(true));
		
		final AtomicReference<String> ar = new AtomicReference<String>("0");
		ValResultCore.<Integer>ofFailure("1").ifSuccessful((a) -> fail("x"), a -> {
			ar.set(a);
		});
		assertThat(ar.get(), is("1"));
	}

	@Test
	public void testIfSuccessfulRunnableConsumerOfString() {
		final AtomicBoolean ab = new AtomicBoolean(false);
		ValResultCore.ofSuccess(123).ifSuccessful((a) -> {
			ab.set(true);
			assertThat(a, is(123));
		}, org.junit.Assert::fail);
		assertThat(ab.get(), is(true));
		
		final AtomicReference<String> ar = new AtomicReference<String>("0");
		ValResultCore.<Integer>ofFailure("1").ifSuccessful((a) -> fail("2"), a -> {
			ar.set(a);
		});
		assertThat(ar.get(), is("1"));
	}

	@Test
	public void testIfFailed() {
		ValResultCore.ofSuccess(123).ifFailed(a-> fail("x"));
		
		final AtomicBoolean ab = new AtomicBoolean(false);
		ValResultCore.ofFailure("xx").ifFailed(a -> {
			ab.set(true);
			assertThat(a, is("xx"));
		});
		assertThat(ab.get(), is(true));
	}

	@Test
	public void testIsSuccessful() {
		assertTrue(ValResultCore.ofSuccess(123).isSuccessful());
		assertFalse(ValResultCore.ofFailure("x").isSuccessful());
	}

	@Test
	public void testMessage() {
		assertThat(ValResultCore.ofSuccess(123).message(), is(nullValue()));
		assertThat(ValResultCore.ofFailure("x").message(), is("x"));
	}

	@Test
	public void testCanBacktrack() {
		assertTrue(ValResultCore.ofSuccess(123).canBacktrack());
		assertTrue(ValResultCore.ofFailure("x").canBacktrack());
		assertTrue(ValResultCore.ofSuccess(456).allowBacktrack(true).canBacktrack());
		assertTrue(ValResultCore.ofFailure("x").allowBacktrack(true).canBacktrack());
		assertFalse(ValResultCore.ofSuccess(789).allowBacktrack(false).canBacktrack());
		assertFalse(ValResultCore.ofFailure("x").allowBacktrack(false).canBacktrack());
	}

}
