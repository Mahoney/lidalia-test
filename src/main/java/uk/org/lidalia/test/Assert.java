package uk.org.lidalia.test;

import static java.lang.reflect.Modifier.isFinal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

public final class Assert {

    public static void assertFinal(final Class<?> theClass, final String methodName, final Class<?>... parameterTypes)
            throws NoSuchMethodException {
        assertTrue(methodName + " is not final", isFinal(theClass.getMethod(methodName, parameterTypes).getModifiers()));
    }

    public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
        assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);

        final InvocationTargetException invocationTargetException = ShouldThrow.shouldThrow(
                InvocationTargetException.class,
                "Able to instantiate " + classThatShouldNotBeInstantiable,
                new Callable<Void>() {
                    public Void call() throws NoSuchMethodException, InvocationTargetException,
                            InstantiationException, IllegalAccessException {
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
        final Throwable cause = invocationTargetException.getCause();
        assertEquals(UnsupportedOperationException.class, cause.getClass());
        assertEquals("Not instantiable", cause.getMessage());
    }

    private static void assertOnlyHasNoArgsConstructor(final Class<?> classThatShouldNotBeInstantiable) {
        assertEquals(Object.class, classThatShouldNotBeInstantiable.getSuperclass());
        assertEquals(1, classThatShouldNotBeInstantiable.getDeclaredConstructors().length);
        final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        assertEquals(0, constructor.getParameterTypes().length);
    }

    private Assert() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
