package uk.org.lidalia.test;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.CombinableMatcher;

public final class Assert {

    public static void assertFinal(final Class<?> theClass, final String methodName, final Class<?>... parameterTypes)
            throws NoSuchMethodException {
        assertTrue(methodName + " is not final", isFinal(theClass.getMethod(methodName, parameterTypes).getModifiers()));
    }

    public static void assertNotInstantiable(final Class<?> classThatShouldNotBeInstantiable) throws Throwable {
        assertThat(classThatShouldNotBeInstantiable, isNotInstantiable());
    }

    private static void assertOnlyHasNoArgsConstructor(final Class<?> classThatShouldNotBeInstantiable) {
        assertEquals(Object.class, classThatShouldNotBeInstantiable.getSuperclass());
        assertEquals(1, classThatShouldNotBeInstantiable.getDeclaredConstructors().length);
        final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructors()[0];
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        assertEquals(0, constructor.getParameterTypes().length);
    }

    private static Matcher<Class<?>> onlyHasNoArgsConstructor() {
        Member x = Object.class.getEnclosingConstructor();
        List<? extends Member> members = asList(Object.class.getDeclaredConstructors());
        return both(directlyExtends(Object.class))
                .and(constructors(Assert.<List<Constructor<?>>>length(is(1))))
                .and(constructors(Assert.<Constructor<?>>at(0, hasModifier(PRIVATE)))
                .and(constructors(at(0, parameterTypes(length(is(0)))));
    }

    private static Matcher<? extends Member> hasModifier(int modifier) {
        return new TypeSafeDiagnosingMatcher<Member>() {

            @Override
            protected boolean matchesSafely(Member item, Description mismatchDescription) {
                return Modifier.isAbstract();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void describeTo(Description description) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }
    }

    private static <E> Matcher<List<? extends E>> at(final Integer index, Matcher<E> matcher) {
        return new FeatureMatcher<List<? extends E>, E>(matcher, "", "") {
            @Override
            protected E featureValueOf(List<? extends E> actual) {
                return actual.get(index);
            }
        };
    }

    private static <T extends Collection<?>> Matcher<T> length(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<T, Integer>(integerMatcher, "", "") {
            @Override
            protected Integer featureValueOf(T actual) {
                return actual.size();
            }
        };
    }

    private static FeatureMatcher<Class<?>, Class> directlyExtends(final Class superType) {
        return new FeatureMatcher<Class<?>, Class>(equalTo(superType), "", "") {
            @Override
            protected Class featureValueOf(Class actual) {
                return actual.getSuperclass();
            }
        };
    }

    private static FeatureMatcher<Class<?>, List<Constructor<?>>> constructors(Matcher<List<Constructor<?>>> matcher) {
        return new FeatureMatcher<Class<?>, List<Constructor<?>>>(matcher, "", "") {
            @Override
            protected List<Constructor<?>> featureValueOf(Class<?> actual) {
                return asList(actual.getDeclaredConstructors());
            }
        };
    }

    public static Matcher<Class<?>> isNotInstantiable() {
        both(onlyHasNoArgsConstructor())
        return new CombinableMatcher<Class<?>>(hasSuperClass(Object.class)).and().and();
        return new BaseMatcher<Class<?>>() {
            @Override
            public boolean matches(Object item) {
//                assertOnlyHasNoArgsConstructor(classThatShouldNotBeInstantiable);
//
//                final InvocationTargetException invocationTargetException = ShouldThrow.shouldThrow(
//                        InvocationTargetException.class,
//                        "Able to instantiate " + classThatShouldNotBeInstantiable,
//                        new Callable<Void>() {
//                            public Void call() throws NoSuchMethodException, InvocationTargetException,
//                                    InstantiationException, IllegalAccessException {
//                                final Constructor<?> constructor = classThatShouldNotBeInstantiable.getDeclaredConstructor();
//                                try {
//                                    constructor.setAccessible(true);
//                                    constructor.newInstance();
//                                    return null;
//                                } finally {
//                                    constructor.setAccessible(false);
//                                }
//                            }
//                        });
//                final Throwable cause = invocationTargetException.getCause();
//                assertEquals(UnsupportedOperationException.class, cause.getClass());
//                assertEquals("Not instantiable", cause.getMessage());
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void describeTo(Description description) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };  //To change body of created methods use File | Settings | File Templates.
    }

    private static Matcher<? super Class<?>> hasSuperClass(Class<Object> objectClass) {

    }

    private Assert() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
