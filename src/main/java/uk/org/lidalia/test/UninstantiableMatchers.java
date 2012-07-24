package uk.org.lidalia.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

public class UninstantiableMatchers {

    public static Matcher<Class<?>> isNotInstantiable() {
        return new CompositeMatcher<Class<?>>(new CompositeMatcher.MatcherFactory<Class<?>>() {
            @Override
            public Matcher<Class<?>> make(Object toMatchAgainst) {
                return new SuperClassMatcher(Object.class);
            }
        }, new CompositeMatcher.MatcherFactory<Class<?>>() {
            @Override
            public Matcher<Class<?>> make(Object toMatchAgainst) {
                return new OnePrivateNoArgsConstructorMatcher();
            }
        }, new CompositeMatcher.MatcherFactory<Class<?>>() {
            @Override
            public Matcher<Class<?>> make(Object toMatchAgainst) {
                Class<?> classToMatchAgainst = (Class<?>) toMatchAgainst;
                return new ConstructorThrowableMatcher(classToMatchAgainst);
            }
        });
    }

    private static class OnePrivateNoArgsConstructorMatcher extends BaseMatcher<Class<?>> {
        @Override
        public boolean matches(Object o) {
            Class<?> classThatShouldNotBeInstantiable = (Class<?>) o;
            if (classThatShouldNotBeInstantiable.getDeclaredConstructors().length != 1) {
                return false;
            }
            final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
            return constructor.getParameterTypes().length == 0 && Modifier.isPrivate(constructor.getModifiers());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("class with 1 private no args constructor");
        }
    }

    private static class ConstructorThrowableMatcher extends BaseMatcher<Class<?>> {

        private final Matcher<Object> throwableClassMatcher = is(UnsupportedOperationException.class);
        private final Matcher<String> messageMatcher;

        private ConstructorThrowableMatcher(Class<?> matchedClass) {
            this.messageMatcher = is(matchedClass.getSimpleName() + " is not instantiable");
        }

        @Override
        public boolean matches(Object o) {
            Class<?> classThatShouldNotBeInstantiable = (Class<?>) o;
            try {
                Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructor();
                constructor.setAccessible(true);
                try {
                    constructor.newInstance();
                } finally {
                    constructor.setAccessible(false);
                }
                return false;
            } catch (InvocationTargetException invocationTargetException) {
                Throwable cause = invocationTargetException.getCause();
                return throwableClassMatcher.matches(cause) && messageMatcher.matches(cause.getMessage());
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("calling that constructor throws a throwable which ").appendDescriptionOf(throwableClassMatcher).appendText(" and whose message ").appendDescriptionOf(messageMatcher);
        }
    }

    private static class SuperClassMatcher extends BaseMatcher<Class<?>> {

        private final Class<?> expectedSuperClass;

        private SuperClassMatcher(Class<?> expectedSuperClass) {
            this.expectedSuperClass = expectedSuperClass;
        }

        @Override
        public boolean matches(Object o) {
            Class<?> classToMatch = (Class<?>) o;
            return classToMatch.getSuperclass() == expectedSuperClass;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("class extending " + expectedSuperClass.getName());
        }
    }
}
