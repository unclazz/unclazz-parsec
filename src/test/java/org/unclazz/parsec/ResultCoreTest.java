package org.unclazz.parsec;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class ResultCoreTest {

	@Test
	public void testOfSuccess() {
		final ResultCore rc0 = ResultCore.ofSuccess();
		assertThat(rc0.isSuccessful(), is(true));
		assertThat(rc0.message(), is(nullValue()));
		assertThat(rc0.canBacktrack(), is(true));
	}

	@Test
	public void testOfFailure() {
		final ResultCore rc1 = ResultCore.ofFailure("an error has occurred.");
		assertThat(rc1.isSuccessful(), is(false));
		assertThat(rc1.message(), is("an error has occurred."));
		assertThat(rc1.canBacktrack(), is(true));
	}

	@Test
	public void testResultCore() {
		final ResultCore rc0 = new ResultCore(true, null, true);
		final ResultCore rc1 = new ResultCore(false, "error", true);
		final ResultCore rc2 = new ResultCore(true, null, false);
		final ResultCore rc3 = new ResultCore(false, "error", false);
		
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
	}

	@Test
	public void testAttachPosition() {
		final Result r = ResultCore.ofSuccess()
				.attachPosition(CharPosition.ofBof().nextColumn(), 
						CharPosition.ofBof().nextColumn().nextLine());
		
		assertThat(r.start().index(), is(1));
		assertThat(r.end().index(), is(2));
	}

	@Test
	public void testAllowBacktrack() {
		final ResultCore rc0 = ResultCore.ofSuccess();
		assertThat(rc0.canBacktrack(), is(true));
		assertThat(rc0.allowBacktrack(true).canBacktrack(), is(true));
		assertThat(rc0.allowBacktrack(false).canBacktrack(), is(false));
		
		final ResultCore rc1 = ResultCore.ofFailure("an error has occurred.");
		assertThat(rc1.canBacktrack(), is(true));
		assertThat(rc1.allowBacktrack(true).canBacktrack(), is(true));
		assertThat(rc1.allowBacktrack(false).canBacktrack(), is(false));
	}

	@Test
	public void testIfSuccessfulRunnable() {
		final AtomicBoolean ab = new AtomicBoolean(false);
		ResultCore.ofSuccess().ifSuccessful(() -> {
			ab.set(true);
		});
		assertThat(ab.get(), is(true));
		
		final AtomicReference<String> ar = new AtomicReference<String>("0");
		ResultCore.ofFailure("1").ifSuccessful(() -> fail("x"), a -> {
			ar.set(a);
		});
		assertThat(ar.get(), is("1"));
	}

	@Test
	public void testIfSuccessfulRunnableConsumerOfString() {
		final AtomicBoolean ab = new AtomicBoolean(false);
		ResultCore.ofSuccess().ifSuccessful(() -> {
			ab.set(true);
		}, org.junit.Assert::fail);
		assertThat(ab.get(), is(true));
		
		final AtomicReference<String> ar = new AtomicReference<String>("0");
		ResultCore.ofFailure("1").ifSuccessful(() -> fail("2"), a -> {
			ar.set(a);
		});
		assertThat(ar.get(), is("1"));
	}

	@Test
	public void testIfFailed() {
		ResultCore.ofSuccess().ifFailed(a-> fail("x"));
		final AtomicBoolean ab = new AtomicBoolean(false);
		ResultCore.ofFailure("xx").ifFailed(a -> {
			ab.set(true);
			assertThat(a, is("xx"));
		});
		assertThat(ab.get(), is(true));
	}

	@Test
	public void testIsSuccessful() {
		assertTrue(ResultCore.ofSuccess().isSuccessful());
		assertFalse(ResultCore.ofFailure("x").isSuccessful());
	}

	@Test
	public void testMessage() {
		assertThat(ResultCore.ofSuccess().message(), is(nullValue()));
		assertThat(ResultCore.ofFailure("x").message(), is("x"));
	}

	@Test
	public void testCanBacktrack() {
		assertTrue(ResultCore.ofSuccess().canBacktrack());
		assertTrue(ResultCore.ofFailure("x").canBacktrack());
		assertTrue(ResultCore.ofSuccess().allowBacktrack(true).canBacktrack());
		assertTrue(ResultCore.ofFailure("x").allowBacktrack(true).canBacktrack());
		assertFalse(ResultCore.ofSuccess().allowBacktrack(false).canBacktrack());
		assertFalse(ResultCore.ofFailure("x").allowBacktrack(false).canBacktrack());
	}

}
