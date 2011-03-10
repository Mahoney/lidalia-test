package uk.org.lidalia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class Assert {

	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(Class<ThrowableType> expectedThrowableType, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		return shouldThrow(expectedThrowableType, null, workThatShouldThrowThrowable);
	}

	@SuppressWarnings("unchecked")
	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(Class<ThrowableType> expectedThrowableType, String message, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		try {
			workThatShouldThrowThrowable.call();
		} catch (Throwable actualThrowableThrown) {
			if (instanceOf(actualThrowableThrown, expectedThrowableType)) {
				return (ThrowableType) actualThrowableThrown;
			} else {
				throw actualThrowableThrown;
			}
		}
		String realMessage = message == null ? "No exception thrown" : message;
		throw new AssertionError(realMessage);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		shouldThrow(expectedThrowable, null, workThatShouldThrowThrowable);
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, String message, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), message, workThatShouldThrowThrowable);
		assertSame(message, expectedThrowable, actualThrowable);
	}

	public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
		assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);

		InvocationTargetException invocationTargetException = shouldThrow(InvocationTargetException.class, new Callable<Void>() {
			public Void call() throws Exception {
				Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance();
				return null;
			}
		});
		UnsupportedOperationException cause = (UnsupportedOperationException) invocationTargetException.getCause();
		assertEquals("Not instantiable", cause.getMessage());
	}

	private static void assertOnlyHasNoArgsConstructor(final Class<?> classThatShouldNotBeInstantiable) {
		assertEquals(Object.class, classThatShouldNotBeInstantiable.getSuperclass());
		assertEquals(1, classThatShouldNotBeInstantiable.getDeclaredConstructors().length);
		final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
		assertEquals(0, constructor.getParameterTypes().length);
	}
	
	private static boolean instanceOf(Object o, Class<?> c) {
		return c.isAssignableFrom(o.getClass());
	}
	
	@SuppressWarnings("unchecked")
	private static <CompileTimeType> Class<? extends CompileTimeType> getClass(final CompileTimeType object) {
		return (Class<? extends CompileTimeType>) object.getClass();
	}

}
