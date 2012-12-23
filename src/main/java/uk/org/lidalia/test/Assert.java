package uk.org.lidalia.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static uk.org.lidalia.test.CombinableMatcher.both;

public final class Assert {

    public static Matcher<Class<?>> isNotInstantiable() {
        return both(hasSuperClassThat(CoreMatchers.<Class<?>>is(Object.class)))
                .and(hasListOfConstructorsWith(both(
                        Assert.<List<Constructor<?>>>length(is(1)))
                        .and(Assert.<List<Constructor<?>>, Constructor<?>>atIndex(0, both(
                                hasConstructorParameterTypes(Assert.<List<Class<?>>>length(is(0))))
                                .and(hasModifier(Modifier.PRIVATE))
                                .and(whichThrowsExceptionThat(both(
                                        isA(UnsupportedOperationException.class))
                                        .and(hasMessageThat(is("Not instantiable")))))))));
    }

    public static FeatureMatcher<Class<?>, Class<?>> hasSuperClassThat(final Matcher<Class<?>> classMatcher) {
        return new FeatureMatcher<Class<?>, Class<?>>(classMatcher, "a Class whose immediate super class", "the immediate super class of") {
            @Override
            protected Class<?> featureValueOf(Class<?> actual) {
                return actual.getSuperclass();
            }
        };
    }

    private static FeatureMatcher<Class<?>, List<Constructor<?>>> hasListOfConstructorsWith(Matcher<List<Constructor<?>>> matcher) {
        return new FeatureMatcher<Class<?>, List<Constructor<?>>>(matcher, "a Class with constructors that are", "the constructors of") {
            @Override
            protected List<Constructor<?>> featureValueOf(Class<?> actual) {
                return asList(actual.getDeclaredConstructors());
            }
        };
    }

    public static <T extends Collection<?>> Matcher<T> length(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<T, Integer>(integerMatcher, "a Collection of length that", "length") {
            @Override
            protected Integer featureValueOf(T actual) {
                return actual.size();
            }
        };
    }

    public static <T extends List<? extends E>, E> Matcher<T> atIndex(final Integer index, Matcher<E> matcher) {
        return new FeatureMatcher<T, E>(matcher, "a List with element at index " + index + " that", "the element at index " + index + " of") {
            @Override
            protected E featureValueOf(T actual) {
                if (actual.size() > index) {
                    return actual.get(index);
                } else {
                    throw new AssertionError(actual + " has no element at index " + index);
                }
            }
        };
    }

    public static <T extends Member> Matcher<T> hasModifier(final Modifier modifier) {
        return new TypeSafeDiagnosingMatcher<T>() {

            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                boolean matches = modifier.isTrueOf(item);
                if (!matches) {
                    mismatchDescription.appendValue(item).appendText(" did not have modifier ").appendValue(modifier);
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("should have modifier " + modifier);
            }
        };
    }

    private static Matcher<Constructor<?>> hasConstructorParameterTypes(Matcher<List<Class<?>>> parameterMatcher) {
        return new FeatureMatcher<Constructor<?>, List<Class<?>>>(parameterMatcher, "has parameter types that are", "parameter types of") {
            @Override
            protected List<Class<?>> featureValueOf(Constructor<?> actual) {
                return asList(actual.getParameterTypes());
            }
        };
    }

    private static <T extends Throwable> Matcher<Constructor<?>> whichThrowsExceptionThat(Matcher<T> throwableMatcher) {
        return new FeatureMatcher<Constructor<?>, T>(throwableMatcher, "which throws an exception that", "the exception thrown by") {
            @Override
            protected T featureValueOf(Constructor<?> constructor) {
                try {
                    constructor.setAccessible(true);
                    constructor.newInstance();
                    return null;
                } catch (InvocationTargetException e) {
                    return (T) e.getCause();
                } catch (Exception e) {
                    throwUnchecked(e);
                    return null;
                } finally {
                    constructor.setAccessible(false);
                }
            }
        };
    }

    private static Matcher<Throwable> hasMessageThat(Matcher<String> messageMatcher) {
        return new FeatureMatcher<Throwable, String>(messageMatcher, "has message that", "message of") {
            @Override
            protected String featureValueOf(Throwable actual) {
                return actual.getMessage();
            }
        };
    }

    private static void throwUnchecked(final Throwable ex) {
        Assert.<RuntimeException>doThrowUnchecked(ex);
        throw new AssertionError("This code should be unreachable. Something went terribly wrong here!");
    }

    private static <T extends Throwable> void doThrowUnchecked(Throwable toThrow) throws T {
        throw (T) toThrow;
    }

    private Assert() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
