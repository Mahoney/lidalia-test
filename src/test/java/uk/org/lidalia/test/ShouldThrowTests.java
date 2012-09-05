package uk.org.lidalia.test;

import java.util.concurrent.Callable;

import org.junit.*;
import org.junit.Assert;

import static uk.org.lidalia.test.Assert.assertNotInstantiable;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class ShouldThrowTests {
    public ShouldThrowTests() {
    }

    @Test
    public void shouldThrowReturnsExpectedException() throws Throwable {
        final NullPointerException expected = new NullPointerException();
        NullPointerException actual = shouldThrow(NullPointerException.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expected;
            }
        });
        org.junit.Assert.assertSame(expected, actual);
    }

    @Test
    public void shouldThrowReturnsExpectedError() throws Throwable {
        final OutOfMemoryError expected = new OutOfMemoryError();
        OutOfMemoryError actual = shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expected;
            }
        });
        Assert.assertSame(expected, actual);
    }

    @Test
    public void shouldThrowThrowsUnexpectedException() throws Throwable {
        final NullPointerException toBeThrown = new NullPointerException();
        try {
            shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw toBeThrown;
                }
            });
            Assert.fail("NullPointerException should have been thrown");
        } catch (NullPointerException npe) {
            Assert.assertSame(toBeThrown, npe);
        }
    }

    @Test
    public void shouldThrowThrowsAssertionFailedErrorIfNoExceptionThrown() throws Throwable {
        try {
            shouldThrow(OutOfMemoryError.class, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occurred");
        } catch (AssertionError error) {
            Assert.assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test
    public void shouldThrowThrowsAssertionFailedErrorWithMessage() throws Throwable {
        try {
            shouldThrow(OutOfMemoryError.class, "Where's out of memory?", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occurred");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's out of memory?", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstancePassesWithExpectedException() throws Throwable {
        final NullPointerException expected = new NullPointerException();
        shouldThrow(expected, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expected;
            }
        });
    }

    @Test
    public void shouldThrowInstancePassesWithExpectedError() throws Throwable {
        final OutOfMemoryError expected = new OutOfMemoryError();
        shouldThrow(expected, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                throw expected;
            }
        });
    }

    @Test
    public void shouldThrowInstanceThrowsUnexpectedException() throws Throwable {
        final NullPointerException toBeThrown = new NullPointerException();
        try {
            shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw toBeThrown;
                }
            });
            Assert.fail("The unexpected null pointer exception should have been thrown");
        } catch (NullPointerException npe) {
            Assert.assertSame(toBeThrown, npe);
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorIfDifferentExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw new OutOfMemoryError();
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Did not throw correct Throwable; expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorIfNoExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfDifferentExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw new OutOfMemoryError();
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's my error? expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfNoExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's my error?", error.getMessage());
        }
    }//NEW

    @Test
    public void shouldThrowReturnsExpectedExceptionForRunnable() throws Throwable {
        final NullPointerException expected = new NullPointerException();
        NullPointerException actual = shouldThrow(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
        Assert.assertSame(expected, actual);
    }

    @Test
    public void shouldThrowReturnsExpectedErrorForRunnable() throws Throwable {
        final OutOfMemoryError expected = new OutOfMemoryError();
        OutOfMemoryError actual = shouldThrow(OutOfMemoryError.class, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
        Assert.assertSame(expected, actual);
    }

    @Test
    public void shouldThrowThrowsUnexpectedExceptionForRunnable() throws Throwable {
        final NullPointerException toBeThrown = new NullPointerException();
        try {
            shouldThrow(OutOfMemoryError.class, new Runnable() {
                @Override
                public void run() {
                    throw toBeThrown;
                }
            });
            Assert.fail("NullPointerException should have been thrown");
        } catch (NullPointerException npe) {
            Assert.assertSame(toBeThrown, npe);
        }
    }

    @Test
    public void shouldThrowThrowsAssertionFailedErrorIfNoExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(OutOfMemoryError.class, new Runnable() {
                @Override
                public void run() {
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test
    public void shouldThrowThrowsAssertionFailedErrorWithMessageForRunnable() throws Throwable {
        try {
            shouldThrow(OutOfMemoryError.class, "Where's out of memory?", new Runnable() {
                @Override
                public void run() {
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's out of memory?", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstancePassesWithExpectedExceptionForRunnable() throws Throwable {
        final NullPointerException expected = new NullPointerException();
        shouldThrow(expected, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
    }

    @Test
    public void shouldThrowInstancePassesWithExpectedErrorForRunnable() throws Throwable {
        final OutOfMemoryError expected = new OutOfMemoryError();
        shouldThrow(expected, new Runnable() {
            @Override
            public void run() {
                throw expected;
            }
        });
    }

    @Test
    public void shouldThrowInstanceThrowsUnexpectedExceptionForRunnable() throws Throwable {
        final NullPointerException toBeThrown = new NullPointerException();
        try {
            shouldThrow(new OutOfMemoryError(), new Runnable() {
                @Override
                public void run() {
                    throw toBeThrown;
                }
            });
            Assert.fail("The unexpected null pointer exception should have been thrown");
        } catch (NullPointerException npe) {
            Assert.assertSame(toBeThrown, npe);
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorIfNoExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), new Runnable() {
                @Override
                public void run() {
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfNoExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Runnable() {
                @Override
                public void run() {
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's my error?", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorIfDifferentExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), new Runnable() {
                @Override
                public void run() {
                    throw new OutOfMemoryError();
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Did not throw correct Throwable; expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test
    public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfDifferentExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Runnable() {
                @Override
                public void run() {
                    throw new OutOfMemoryError();
                }
            });
            Assert.fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            Assert.assertEquals("Where's my error? expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test public void notInstantiable() throws Throwable {
        assertNotInstantiable(ShouldThrow.class);
    }
}
