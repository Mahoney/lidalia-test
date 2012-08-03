package uk.org.lidalia.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.org.lidalia.test.Assert.shouldThrow;

public class UninstantiableMatchers {

    public static Matcher<Class<?>> isNotInstantiable() {
        return NOT_INSTANTIABLE_MATCHER;
    }

    private static final NotInstantiableMatcher NOT_INSTANTIABLE_MATCHER = new NotInstantiableMatcher();

    private static class NotInstantiableMatcher extends BaseMatcher<Class<?>> {
        @Override
        public boolean matches(Object o) {
            Class<?> classToMatchAgainst = (Class<?>) o;
            assertThat(classToMatchAgainst + " has an unexpected superclass",
                    (Class<Object>) classToMatchAgainst.getSuperclass(), sameInstance(Object.class));
            assertThat(classToMatchAgainst + " has the wrong number of constructors",
                    classToMatchAgainst.getDeclaredConstructors().length, is(1));
            final Constructor<?> constructor = classToMatchAgainst.getDeclaredConstructors()[0];
            assertThat(classToMatchAgainst + " has the wrong number of parameters on constructor", constructor.getParameterTypes().length, is(0));
            assertTrue(classToMatchAgainst + "'s constructor should be private", Modifier.isPrivate(constructor.getModifiers()));
            final InvocationTargetException invocationTargetException = shouldThrow(InvocationTargetException.class, "Able to instantiate " + classToMatchAgainst+ " via reflection", new Callable<Void>() {
                public Void call() throws Exception {
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
            assertThat(classToMatchAgainst + "'s constructor threw the wrong exception", cause, is(UnsupportedOperationException.class));
            assertThat(classToMatchAgainst + "'s constructor should throw an exception with a specific message", cause.getMessage(), is(classToMatchAgainst.getSimpleName() + " is not instantiable"));
            return true;
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}
