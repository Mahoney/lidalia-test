package uk.org.lidalia.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static uk.org.lidalia.test.CombinableMatcher.both;

public final class Assert {

    public static Matcher<Class<?>> isNotInstantiable() {
        return both(aClassWhoseSuperClass(is(equalTo(Object.class))))
                .and(aClassWhoseSetOfConstructors(both(
                        is(Assert.<List<Constructor<?>>>aCollectionWhoseLength(is(1))))
                        .and(is(Assert.<List<Constructor<?>>, Constructor<?>>aListWhoseElementAtIndex(0, both(
                                is(aConstructorWhoseParameterTypes(is(Assert.<List<Class<?>>>aCollectionWhoseLength(is(0))))))
                                .and(isAMemberWithModifier(Modifier.PRIVATE))
                                .and(aConstructorWhoseThrownException(both(
                                        isA(UnsupportedOperationException.class))
                                        .and(is(aThrowableWhoseMessage(is("Not instantiable")))))))))));
    }

    public static <U, T extends U> FeatureMatcher<Class<? extends T>, Class<? extends U>> aClassWhoseSuperClass(final Matcher<? extends Class<? extends U>> classMatcher) {
        return new FeatureMatcher<Class<? extends T>, Class<? extends U>>(classMatcher, "a Class whose super class", "'s super class") {
            @Override
            protected Class<? extends U> featureValueOf(Class<? extends T> actual) {
                return (Class<? extends U>) actual.getSuperclass();
            }
        };
    }

    private static FeatureMatcher<Class<?>, List<Constructor<?>>> aClassWhoseSetOfConstructors(Matcher<List<Constructor<?>>> matcher) {
        return new FeatureMatcher<Class<?>, List<Constructor<?>>>(matcher, "a Class whose set of constructors", "'s constructors") {
            @Override
            protected List<Constructor<?>> featureValueOf(Class<?> actual) {
                return asList(actual.getDeclaredConstructors());
            }
        };
    }

    public static <T extends Collection<?>> Matcher<T> aCollectionWhoseLength(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<T, Integer>(integerMatcher, "a Collection whose length", "'s length") {
            @Override
            protected Integer featureValueOf(T actual) {
                return actual.size();
            }
        };
    }

    public static <T extends List<? extends E>, E> Matcher<T> aListWhoseElementAtIndex(final Integer index, Matcher<E> matcher) {
        return new FeatureMatcher<T, E>(matcher, "a List whose element at index " + index, "'s element at index " + index) {
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

    public static <T extends Member> Matcher<T> isAMemberWithModifier(final Modifier modifier) {
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
                description.appendText("is a member with modifier " + modifier);
            }
        };
    }

    private static Matcher<Constructor<?>> aConstructorWhoseParameterTypes(Matcher<List<Class<?>>> parameterMatcher) {
        return new FeatureMatcher<Constructor<?>, List<Class<?>>>(parameterMatcher, "a constructor whose parameter types", "'s parameter types") {
            @Override
            protected List<Class<?>> featureValueOf(Constructor<?> actual) {
                return asList(actual.getParameterTypes());
            }
        };
    }

    private static Matcher<Constructor<?>> aConstructorWhoseThrownException(Matcher<? extends Throwable> throwableMatcher) {
        return new FeatureMatcher<Constructor<?>, Throwable>(throwableMatcher, "a constructor whose thrown exception", "'s thrown exception") {
            @Override
            protected Throwable featureValueOf(Constructor<?> constructor) {
                try {
                    constructor.setAccessible(true);
                    constructor.newInstance();
                    return null;
                } catch (InvocationTargetException e) {
                    return e.getCause();
                } catch (Exception e) {
                    return throwUnchecked(e, null);
                } finally {
                    constructor.setAccessible(false);
                }
            }
        };
    }

    private static Matcher<Throwable> aThrowableWhoseMessage(Matcher<String> messageMatcher) {
        return new FeatureMatcher<Throwable, String>(messageMatcher, "a throwable whose message", "'s message") {
            @Override
            protected String featureValueOf(Throwable actual) {
                return actual.getMessage();
            }
        };
    }

    private static <T> T throwUnchecked(final Throwable ex, T shouldBeNullAndNotUsed) {
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
