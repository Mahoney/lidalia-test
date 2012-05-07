package uk.org.lidalia.test;

import java.util.concurrent.Callable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.org.lidalia.test.Assert.assertFinal;
import static uk.org.lidalia.test.Assert.assertNotInstantiable;
import static uk.org.lidalia.test.Assert.shouldThrow;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class AssertTests {

    @SuppressWarnings("unused")
    private static final class DummyClass {
        public final void finalMethod() {}
        public void nonFinalMethod() {}
        protected final void protectedFinalMethod() {}
        public final void finalMethod(String arg1, Integer arg2) {}
    }

    @Test public void assertFinalWithFinalMethod() throws Exception {
        assertFinal(DummyClass.class, "finalMethod");
    }

    @Test public void assertFinalWithNonFinalMethod() throws Throwable {
        AssertionError error = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                assertFinal(DummyClass.class, "nonFinalMethod");
                return null;
            }
        });
        assertEquals("nonFinalMethod is not final", error.getMessage());
    }

    @Test(expected = NoSuchMethodException.class)
    public void assertFinalWithNonPublicMethod() throws Exception {
        assertFinal(DummyClass.class, "protectedFinalMethod");
    }

    @Test public void assertFinalWithMatchingArgs() throws Exception {
        assertFinal(DummyClass.class, "finalMethod", String.class, Integer.class);
    }

    @Test(expected = NoSuchMethodException.class)
    public void assertFinalWithNonMatchingArgs() throws Exception {
        assertFinal(DummyClass.class, "finalMethod", String.class, String.class);
    }

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
            fail("An assertion failed error should have been thrown as the no throwable occurred");
        } catch (AssertionError error) {
            assertEquals("No exception thrown", error.getMessage());
        }
    }

	@Test public void shouldThrowThrowsAssertionFailedErrorWithMessage() throws Throwable {
		try {
			shouldThrow(OutOfMemoryError.class, "Where's out of memory?", new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					return null;
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occurred");
		} catch (AssertionError error) {
            assertEquals("Where's out of memory?", error.getMessage());
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

	@Test public void shouldThrowInstanceThrowsAssertionFailedErrorIfDifferentExceptionThrown() throws Throwable {
		try {
			shouldThrow(new OutOfMemoryError(), new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					throw new OutOfMemoryError();
				}
			});
			fail("An assertion failed error should have been thrown as the no throwable occured");
		} catch (AssertionError error) {
			assertEquals("Did not throw correct Throwable; expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
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
            assertEquals("No exception thrown", error.getMessage());
        }
    }

    @Test public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfDifferentExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    throw new OutOfMemoryError();
                }
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Where's my error? expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfNoExceptionThrown() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return null;
                }
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Where's my error?", error.getMessage());
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
            assertEquals("No exception thrown", error.getMessage());
		}
	}

    @Test public void shouldThrowThrowsAssertionFailedErrorWithMessageForRunnable() throws Throwable {
        try {
            shouldThrow(OutOfMemoryError.class, "Where's out of memory?", new Runnable() {
                @Override
                public void run() {}
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Where's out of memory?", error.getMessage());
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
            assertEquals("No exception thrown", error.getMessage());
		}
	}

    @Test public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfNoExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Runnable() {
                @Override
                public void run(){}
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Where's my error?", error.getMessage());
        }
    }

    @Test public void shouldThrowInstanceThrowsAssertionFailedErrorIfDifferentExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), new Runnable() {
                @Override
                public void run() {
                    throw new OutOfMemoryError();
                }
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Did not throw correct Throwable; expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test public void shouldThrowInstanceThrowsAssertionFailedErrorWithMessageIfDifferentExceptionThrownForRunnable() throws Throwable {
        try {
            shouldThrow(new OutOfMemoryError(), "Where's my error?", new Runnable() {
                @Override
                public void run(){
                    throw new OutOfMemoryError();
                }
            });
            fail("An assertion failed error should have been thrown as the no throwable occured");
        } catch (AssertionError error) {
            assertEquals("Where's my error? expected same:<java.lang.OutOfMemoryError> was not:<java.lang.OutOfMemoryError>", error.getMessage());
        }
    }

    @Test public void assertNotInstantiableWithUninstantiableClass() throws Throwable {
        assertNotInstantiable(Uninstantiable.class);
    }

    @Test public void assertNotInstantiableWithInstantiableClass() throws Throwable {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertNotInstantiable(Instantiable.class);
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertEquals("Able to instantiate class uk.org.lidalia.test.AssertTests$Instantiable", expected.getMessage());
    }

    private static class Uninstantiable {
        private Uninstantiable() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class Instantiable {
        private Instantiable() {

        }
    }
}
