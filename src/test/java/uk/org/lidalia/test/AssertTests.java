package uk.org.lidalia.test;

import java.util.concurrent.Callable;

import org.junit.Test;
import static uk.org.lidalia.test.Assert.shouldThrow;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class AssertTests {

	@Test public void shouldThrowReturnsExpectedException() throws Throwable {
		final NullPointerException expected = new NullPointerException();
		NullPointerException actual = shouldThrow(NullPointerException.class, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				throw expected;
			}
		});
		assertSame(expected, actual);
	}

	@Test public void shouldThrowReturnsExpectedError() throws Throwable {
		final OutOfMemoryError expected = new OutOfMemoryError();
		OutOfMemoryError actual = shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				throw expected;
			}
		});
		assertSame(expected, actual);
	}

	@Test public void shouldThrowThrowsUnexpectedException() throws Throwable {
		final NullPointerException toBeThrown = new NullPointerException();
		try {
			shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					throw toBeThrown;
				}
			});
			fail("NullPointerException should have been thrown");
		} catch (NullPointerException npe) {
			assertSame(toBeThrown, npe);
		}
	}

	@Test public void shouldThrowThrowsAssertionFailedErrorIfNoExceptionThrown() throws Throwable {
		try {
			shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					return null;
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occured");
		} catch (AssertionError error) {
			assert "No exception thrown".equals(error.getMessage());
		}
	}

	@Test public void shouldThrowInstancePassesWithExpectedException() throws Throwable {
		final NullPointerException expected = new NullPointerException();
		shouldThrow(expected, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				throw expected;
			}
		});
	}

	@Test public void shouldThrowInstancePassesWithExpectedError() throws Throwable {
		final OutOfMemoryError expected = new OutOfMemoryError();
		shouldThrow(expected, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				throw expected;
			}
		});
	}

	@Test public void shouldThrowInstanceThrowsUnexpectedException() throws Throwable {
		final NullPointerException toBeThrown = new NullPointerException();
		try {
			shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					throw toBeThrown;
				}
			});
			fail("The unexpected null pointer exception should have been thrown");
		} catch (NullPointerException npe) {
			assertSame(toBeThrown, npe);
		}
	}

	@Test public void shouldThrowInstanceThrowsAssertionFailedErrorIfNoExceptionThrown() throws Throwable {
		try {
			shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					return null;
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occured");
		} catch (AssertionError error) {
			assert "No exception thrown".equals(error.getMessage());
		}
	}

	//NEW
	@Test public void shouldThrowReturnsExpectedExceptionForRunnable() throws Throwable {
		final NullPointerException expected = new NullPointerException();
		NullPointerException actual = shouldThrow(NullPointerException.class, new Runnable() {
			@Override
			public void run() {
				throw expected;
			}
		});
		assertSame(expected, actual);
	}

	@Test public void shouldThrowReturnsExpectedErrorForRunnable() throws Throwable {
		final OutOfMemoryError expected = new OutOfMemoryError();
		OutOfMemoryError actual = shouldThrow(OutOfMemoryError.class, new Runnable() {
			@Override
			public void run() {
				throw expected;
			}
		});
		assertSame(expected, actual);
	}

	@Test public void shouldThrowThrowsUnexpectedExceptionForRunnable() throws Throwable {
		final NullPointerException toBeThrown = new NullPointerException();
		try {
			shouldThrow(OutOfMemoryError.class, new Runnable() {
				@Override
				public void run() {
					throw toBeThrown;
				}
			});
			fail("NullPointerException should have been thrown");
		} catch (NullPointerException npe) {
			assertSame(toBeThrown, npe);
		}
	}

	@Test public void shouldThrowThrowsAssertionFailedErrorIfNoExceptionThrownForRunnable() throws Throwable {
		try {
			shouldThrow(OutOfMemoryError.class, new Runnable() {
				@Override
				public void run() {
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occured");
		} catch (AssertionError error) {
			assert "No exception thrown".equals(error.getMessage());
		}
	}

	@Test public void shouldThrowInstancePassesWithExpectedExceptionForRunnable() throws Throwable {
		final NullPointerException expected = new NullPointerException();
		shouldThrow(expected, new Runnable() {
			@Override
			public void run() {
				throw expected;
			}
		});
	}

	@Test public void shouldThrowInstancePassesWithExpectedErrorForRunnable() throws Throwable {
		final OutOfMemoryError expected = new OutOfMemoryError();
		shouldThrow(expected, new Runnable() {
			@Override
			public void run() {
				throw expected;
			}
		});
	}

	@Test public void shouldThrowInstanceThrowsUnexpectedExceptionForRunnable() throws Throwable {
		final NullPointerException toBeThrown = new NullPointerException();
		try {
			shouldThrow(new OutOfMemoryError(), new Runnable() {
				@Override
				public void run() {
					throw toBeThrown;
				}
			});
			fail("The unexpected null pointer exception should have been thrown");
		} catch (NullPointerException npe) {
			assertSame(toBeThrown, npe);
		}
	}

	@Test public void shouldThrowInstanceThrowsAssertionFailedErrorIfNoExceptionThrownForRunnable() throws Throwable {
		try {
			shouldThrow(new OutOfMemoryError(), new Runnable() {
				@Override
				public void run() {
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occured");
		} catch (AssertionError error) {
			assert "No exception thrown".equals(error.getMessage());
		}
	}
}
