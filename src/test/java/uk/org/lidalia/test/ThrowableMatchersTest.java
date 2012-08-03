package uk.org.lidalia.test;

import org.junit.Test;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.org.lidalia.test.ThrowableMatchers.doesThrow;

public class ThrowableMatchersTest {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test public void doesThrowWithExpectedException() throws Throwable {
        final NullPointerException expectedException = new NullPointerException();
        assertThat(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expectedException;
            }
        }, doesThrow(expectedException));
    }

    @Test public void doesThrowWithExpectedError() throws Throwable {
        final OutOfMemoryError expectedError = new OutOfMemoryError();
        assertThat(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expectedError;
            }
        }, doesThrow(expectedError));
    }

    @Test public void doesThrowWithUnexpectedExceptionType() throws Throwable {
        final NullPointerException toBeThrown = new NullPointerException();
        try {
            assertThat(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw toBeThrown;
                }
            }, doesThrow(new OutOfMemoryError()));
            fail("The unexpected null pointer exception should have been thrown");
        } catch (NullPointerException thrownException) {
            assertThat(thrownException, is(sameInstance(toBeThrown)));
        }
    }

    @Test public void doesThrowWithDifferentInstanceOfSameType() throws Throwable {
        try {
            assertThat(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw new Exception("actual exception");
                }
            }, doesThrow(new Exception("expected exception")));
            fail("An assertion failed error should have been thrown as a different throwable occured");
        } catch (AssertionError error) {
            assertThat(error.getMessage(), is(LINE_SEPARATOR +
                    "Expected: is same(<java.lang.Exception: expected exception>)" + LINE_SEPARATOR +
                    "     got: <java.lang.Exception: actual exception>" + LINE_SEPARATOR
            ));
        }
    }

    @Test public void doesThrowWithNoExceptionThrown() throws Throwable {
        try {
            assertThat(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            }, doesThrow(new OutOfMemoryError()));
            fail("An assertion failed error should have been thrown as the no throwable occurred");
        } catch (AssertionError error) {
            assertThat(error.getMessage(), is("No exception thrown"));
        }
    }
}
