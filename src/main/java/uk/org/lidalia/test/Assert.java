package uk.org.lidalia.test;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.lang.reflect.Modifier.isFinal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

import com.google.common.base.Optional;

public class Assert {

	private static final String INVOKER_INVOCATION_EXCEPTION_CLASSNAME = "org.codehaus.groovy.runtime.InvokerInvocationException";

    public static void assertFinal(Class<?> theClass, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        assertTrue(methodName + " is not final", isFinal(theClass.getMethod(methodName, parameterTypes).getModifiers()));
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Runnable workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final String message, final Runnable workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, of(message), workThatShouldThrowThrowable);
    }

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
	}

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final String message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, of(message), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final String message, final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, of(message), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final String message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, of(message), workThatShouldThrowThrowable);
    }

    private static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Optional<String> message, final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, message, toCallable(workThatShouldThrowThrowable));
    }

    private static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Optional<String> message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        final ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), message, workThatShouldThrowThrowable);
        assertSame(message.or("Did not throw correct Throwable;"), expectedThrowable, actualThrowable);
    }

    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Optional<String> message, final Runnable workThatShouldThrowThrowable) throws Throwable {
        final Callable<Void> workAsCallable = toCallable(workThatShouldThrowThrowable);
        return shouldThrow(expectedThrowableType, message, workAsCallable);
    }

    private static Callable<Void> toCallable(final Runnable workThatShouldThrowThrowable) {
        return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    workThatShouldThrowThrowable.run();
                    return null;
                }
            };
    }

    @SuppressWarnings("unchecked")
    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Optional<String> message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        try {
            workThatShouldThrowThrowable.call();
        } catch (final Throwable actualThrowableThrown) {
            final Throwable trueThrowable = extractTrueThrowable(expectedThrowableType, actualThrowableThrown);
            if (instanceOf(trueThrowable, expectedThrowableType)) {
                return (ThrowableType) trueThrowable;
            }
            throw actualThrowableThrown;
        }
        throw new AssertionError(message.or("No exception thrown"));
    }

    private static <ThrowableType extends Throwable> Throwable extractTrueThrowable(Class<ThrowableType> expectedThrowableType, Throwable actualThrowableThrown) {
        final Throwable trueThrowable;
        if (actualThrowableThrown.getClass().getName().equals(INVOKER_INVOCATION_EXCEPTION_CLASSNAME) && !expectedThrowableType.getName().equals(INVOKER_INVOCATION_EXCEPTION_CLASSNAME)) {
            trueThrowable = actualThrowableThrown.getCause();
        } else {
            trueThrowable = actualThrowableThrown;
        }
        return trueThrowable;
    }

	public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
		assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);

		final InvocationTargetException invocationTargetException = shouldThrow(InvocationTargetException.class, "Able to instantiate " + classThatShouldNotBeInstantiable, new Callable<Void>() {
			public Void call() throws Exception {
                final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructor();
                try {
                    constructor.setAccessible(true);
                    constructor.newInstance();
                    return null;
                } finally {
                    constructor.setAccessible(false);
                }
			}
		});
		final UnsupportedOperationException cause = (UnsupportedOperationException) invocationTargetException.getCause();
		assertEquals("Not instantiable", cause.getMessage());
	}

	private static void assertOnlyHasNoArgsConstructor(final Class<?> classThatShouldNotBeInstantiable) {
		assertEquals(Object.class, classThatShouldNotBeInstantiable.getSuperclass());
		assertEquals(1, classThatShouldNotBeInstantiable.getDeclaredConstructors().length);
		final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		assertEquals(0, constructor.getParameterTypes().length);
	}

	private static boolean instanceOf(final Object o, final Class<?> c) {
		return c.isAssignableFrom(o.getClass());
	}

	@SuppressWarnings("unchecked")
	private static <CompileTimeType> Class<? extends CompileTimeType> getClass(final CompileTimeType object) {
		return (Class<? extends CompileTimeType>) object.getClass();
	}

}
