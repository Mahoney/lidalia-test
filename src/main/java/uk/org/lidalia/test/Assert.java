package uk.org.lidalia.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

public class Assert {

	@SuppressWarnings("unchecked")
	public static <ThrowableType extends Throwable> ThrowableType shouldThrow(Class<ThrowableType> expectedThrowableType, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		try {
			workThatShouldThrowThrowable.call();
		} catch (Throwable actualThrowableThrown) {
			if (instanceOf(actualThrowableThrown, expectedThrowableType)) {
				return (ThrowableType) actualThrowableThrown;
			} else {
				throw actualThrowableThrown;
			}
		}
		throw new AssertionError("No exception thrown");
	}

	public static <ThrowableType extends Throwable> void shouldThrow(final ThrowableType expectedThrowable, Callable<Void> workThatShouldThrowThrowable) throws Throwable {
		ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), workThatShouldThrowThrowable);
		assertSame(expectedThrowable, actualThrowable);
	}

	public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
		assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);

		UnsupportedOperationException oue = shouldThrow(UnsupportedOperationException.class, new Callable<Void>() {
			public Void call() throws Exception {
				Constructor<?> constructor = classThatShouldNotBeInstantiable.getConstructor();
				constructor.setAccessible(true);
				constructor.newInstance();
				return null;
			}
		});
		assertEquals("Not instantiable", oue.getMessage());
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
