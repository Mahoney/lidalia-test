package uk.org.lidalia.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class ShouldThrowTests {

    @Test
    public void shouldThrowReturnsExpectedException() {
        final NullPointerException expected = new NullPointerException();
        NullPointerException actual = shouldThrow(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
        assertSame(expected, actual);
    }

    @Test
    public void shouldThrowReturnsExpectedError() {
        final OutOfMemoryError expected = new OutOfMemoryError();
        OutOfMemoryError actual = shouldThrow(OutOfMemoryError.class, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
        assertSame(expected, actual);
    }

    @Test
    public void shouldThrowThrowsUnexpectedException() {
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

    @Test
    public void shouldThrowThrowsAssertionFailedErrorIfNoExceptionThrown() {
        try {
            shouldThrow(OutOfMemoryError.class, new Runnable() {
                @Override
                public void run() {
                    // do nothing
                }
            });
            fail("An assertion failed error should have been thrown as the no throwable occurred");
        } catch (AssertionError error) {
            assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test
    public void shouldThrowThrowsAssertionFailedErrorWithMessage() {
        try {
            shouldThrow(OutOfMemoryError.class, "Where's out of memory?", new Runnable() {
                @Override
                public void run() {
                    // do nothing
                }
            });
            fail("An assertion failed error should have been thrown as no throwable was thrown");
        } catch (AssertionError error) {
            assertEquals("Where's out of memory?", error.getMessage());
        }
    }

    @Test public void notInstantiable() {
        assertThat(ShouldThrow.class, uk.org.lidalia.test.Assert.isNotInstantiable());
    }
}
