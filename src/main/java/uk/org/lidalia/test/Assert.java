package uk.org.lidalia.test;

import groovy.lang.Closure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class Assert {

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, null, workThatShouldThrowThrowable);
	}

	@SuppressWarnings("unchecked")
	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final String message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		try {
			workThatShouldThrowThrowable.call();
		} catch (Throwable actualThrowableThrown) {
			if (instanceOf(actualThrowableThrown, expectedThrowableType)) {
				return (ThrowableType) actualThrowableThrown;
			} else {
				throw actualThrowableThrown;
			}
		}
		final String realMessage = message == null ? "No exception thrown" : message;
		throw new AssertionError(realMessage);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		shouldThrow(expectedThrowable, null, workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final String message, final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		final ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), message, workThatShouldThrowThrowable);
		assertSame(message, expectedThrowable, actualThrowable);
	}

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Runnable workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, null, workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final String message, final Runnable workThatShouldThrowThrowable) throws Throwable {
		final Callable<Void> workAsCallable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				workThatShouldThrowThrowable.run();
				return null;
			}
		};
		return shouldThrow(expectedThrowableType, message, workAsCallable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Runnable workThatShouldThrowThrowable) throws Throwable {
		shouldThrow(expectedThrowable, null, workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final String message, final Runnable workThatShouldThrowThrowable) throws Throwable {
		final ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), message, workThatShouldThrowThrowable);
		assertSame(message, expectedThrowable, actualThrowable);
	}

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final Closure workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, (Runnable) workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(final Class<ThrowableType> expectedThrowableType, final String message, final Closure workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, message, (Runnable) workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final Closure workThatShouldThrowThrowable) throws Throwable {
		shouldThrow(expectedThrowable, (Runnable) workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, final String message, final Closure workThatShouldThrowThrowable) throws Throwable {
		shouldThrow(expectedThrowable, message, (Runnable) workThatShouldThrowThrowable);
	}

	@SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
	public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
		assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);

		final InvocationTargetException invocationTargetException = shouldThrow(InvocationTargetException.class, new Callable<Void>() {
			public Void call() throws Exception {
				final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance();
				return null;
			}
		});
		final UnsupportedOperationException cause = (UnsupportedOperationException) invocationTargetException.getCause();
		assertEquals("Not instantiable", cause.getMessage());
	}

	private static void assertOnlyHasNoArgsConstructor(final Class<?> classThatShouldNotBeInstantiable) {
		assertEquals(Object.class, classThatShouldNotBeInstantiable.getSuperclass());
		assertEquals(1, classThatShouldNotBeInstantiable.getDeclaredConstructors().length);
		final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
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
