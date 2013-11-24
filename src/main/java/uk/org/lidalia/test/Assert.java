package uk.org.lidalia.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import uk.org.lidalia.lang.Modifier;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static uk.org.lidalia.lang.Exceptions.throwUnchecked;

/**
 * Utility Hamcrest matchers for unit test assertions.
 */
public final class Assert {

    /**
     * Asserts that a class is not instantiable - it exists only for its static members.
     * <p>
     * More precisely, asserts that the class:
     * <ul>
     *     <li>Has Object as its immediate superclass</li>
     *     <li>Has only one constructor</li>
     *     <li>That constructor is private</li>
     *     <li>That constructor takes no arguments</li>
     *     <li>That constructor will throw an {@link UnsupportedOperationException} with message "Not Instantiable" if it is
     *     invoked via reflection.</li>
     * </ul>
     * <p>
     * Usage:
     * {@code assertThat(Values.class, isNotInstantiable());}
     *
     * @return a matcher that asserts that a class is not instantiable
     */
    public static Matcher<Class<?>> isNotInstantiable() {
        final Matcher isAThrowableWhoseMessageIs = is(aThrowableWhoseMessage(is("Not instantiable")));

        final Matcher isAnUnsupportedOperationException = instanceOf(UnsupportedOperationException.class);

        final CombinableMatcher bothIsAnUnsupportedOperationException = CombinableMatcher.both(
                isAThrowableWhoseMessageIs);

        final CombinableMatcher bothIsAnUnsupportedOperationExceptionAndIsAThrowableWhoseMessageIs =
                bothIsAnUnsupportedOperationException
                        .and(isAnUnsupportedOperationException);

        final Matcher aConstructorWhoseThrownExceptionBothIsAnUnsupportedOperationExceptionAndIsAThrowableWhoseMessageIs
                = aConstructorWhoseThrownException(bothIsAnUnsupportedOperationExceptionAndIsAThrowableWhoseMessageIs);

        final Matcher aMemberWithModifierPrivate = isAMemberWithModifier(Modifier.PRIVATE);

        final Matcher isACollectionWhoseSizeIs0 = is(Assert.<List<Class<?>>>aCollectionWhoseSize(is(0)));

        final Matcher isAConstructorWhoseParameterTypesAreACollectionWhoseSizeIs0 = is(aConstructorWhoseParameterTypes(isACollectionWhoseSizeIs0));

        final CombinableMatcher<Constructor<?>> isAPrivateNoArgsConstructor = CombinableMatcher.both(
                isAConstructorWhoseParameterTypesAreACollectionWhoseSizeIs0)
                .and(aMemberWithModifierPrivate)
                .and(aConstructorWhoseThrownExceptionBothIsAnUnsupportedOperationExceptionAndIsAThrowableWhoseMessageIs);

        final Matcher<Class<Object>> isEqualToObjectClass = is(equalTo(Object.class));
        final FeatureMatcher<Class<?>, Class<?>> isAClassThatExtendsObjectDirectly = aClassWhoseSuperClass(isEqualToObjectClass);
        final Matcher<List<Constructor<?>>> aSingleElementCollection = Assert.aCollectionWhoseSize(is(1));
        final Matcher<List<Constructor<?>>> isASingleElementCollection = is(aSingleElementCollection);
        final Matcher<List<Constructor<?>>> aListWhoseFirstElementIsAPrivateNoArgsConstructor = Assert.<List<Constructor<?>>, Constructor<?>>aListWhoseElementAtIndex(0, isAPrivateNoArgsConstructor);
        final Matcher<List<Constructor<?>>> isAListWhoseFirstElementIsAPrivateNoArgsConstructor = is(aListWhoseFirstElementIsAPrivateNoArgsConstructor);

        final CombinableMatcher<List<Constructor<?>>> both = CombinableMatcher.both(
                isASingleElementCollection);
        final CombinableMatcher<List<Constructor<?>>> isASinglePrivateNoArgsConstructor = both
                .and(isAListWhoseFirstElementIsAPrivateNoArgsConstructor);
        final FeatureMatcher<Class<?>, List<Constructor<?>>> isAClassWithASinglePrivateNoArgsConstructor = aClassWhoseSetOfConstructors(isASinglePrivateNoArgsConstructor);
        final CombinableMatcher<Class<?>> both1 = CombinableMatcher.both(isAClassThatExtendsObjectDirectly);
        final CombinableMatcher<Class<?>> and = both1.and(isAClassWithASinglePrivateNoArgsConstructor);

        return (CombinableMatcher<Class<?>>) and;
    }

    /**
     * Facilitates making an assertion about the superclass of a {@link Class}.
     * <p>
     * Usage:
     * {@code assertThat(String.class, is(aClassWhoseSuperClass(is(Object.class))));}
     *
     * @param classMatcher the matcher that will be applied to the class's superclass
     * @return a matcher that will assert something about the superclass of a class
     */
    private static FeatureMatcher<Class<?>, Class<?>> aClassWhoseSuperClass(
            final Matcher<? extends Class<?>> classMatcher) {
        return new FeatureMatcher<Class<?>, Class<?>>(
                classMatcher, "a Class whose super class", "'s super class") {
            @Override @SuppressWarnings("unchecked")
            protected Class<?> featureValueOf(final Class<?> actual) {
                return actual.getSuperclass();
            }
        };
    }

    private static FeatureMatcher<Class<?>, List<Constructor<?>>> aClassWhoseSetOfConstructors(
            final Matcher<List<Constructor<?>>> matcher) {
        return new FeatureMatcher<Class<?>, List<Constructor<?>>>(
                matcher, "a Class whose set of constructors", "'s constructors") {
            @Override
            protected List<Constructor<?>> featureValueOf(final Class<?> actual) {
                final List<Constructor<?>> constructors = asList(actual.getDeclaredConstructors());
                Collections.sort(constructors, new Comparator<Constructor<?>>() {
                    @Override
                    public int compare(final Constructor<?> one, final Constructor<?> other) {
                        return one.toString().compareTo(other.toString());
                    }
                });
                return constructors;
            }
        };
    }

    /**
     * Facilitates making an assertion about the size of a {@link Collection}.
     * <p>
     * Usage:
     * {@code assertThat(asList(1, 2, 3), is(aCollectionWhoseSize(is(3))));}
     *
     * @param sizeMatcher the matcher that will be applied to the collection's size
     * @param <T> the type of the collection whose size will be matched
     * @return a matcher that will assert something about a collection's size
     */
    public static <T extends Collection<?>> Matcher<T> aCollectionWhoseSize(final Matcher<Integer> sizeMatcher) {
        return new FeatureMatcher<T, Integer>(sizeMatcher, "a Collection whose size", "'s size") {
            @Override
            protected Integer featureValueOf(final T actual) {
                return actual.size();
            }
        };
    }

    /**
     * Facilitates making an assertion about the element at a given index of a {@link List}.
     * <p>
     * Usage:
     * {@code assertThat(asList("a", "b", "c"), is(aListWhoseElementAtIndex(2, is("c"))));}
     *
     * @param index the index of the element in the list the matcher will be applied to
     * @param matcher the matcher that will be applied to the element
     * @param <T> the type of the List
     * @param <E> the type of the elements in the List
     * @return a matcher that will assert something about the element at the given index of a collection
     */
    public static <T extends List<? extends E>, E> Matcher<T> aListWhoseElementAtIndex(
            final Integer index, final Matcher<E> matcher) {
        return new FeatureMatcher<T, E>(matcher, "a List whose element at index " + index, "'s element at index " + index) {
            @Override
            protected E featureValueOf(final T actual) {
                if (actual.size() > index) {
                    return actual.get(index);
                } else {
                    throw new AssertionError(actual + " has no element at index " + index);
                }
            }
        };
    }

    /**
     * Asserts that a given {@link Member} has a given {@link Modifier}.
     * <p>
     * Usage:
     * {@code assertThat(Object.class.getMethod("toString"), isAMemberWithModifier(Modifier.PUBLIC));}
     *
     * @param modifier the modifier the member is expected to have
     * @param <T> the type of the Member
     * @return a matcher that will assert a member has a modifier
     */
    public static <T extends Member> Matcher<T> isAMemberWithModifier(final Modifier modifier) {
        return new TypeSafeDiagnosingMatcher<T>() {

            @Override
            protected boolean matchesSafely(final T item, final Description mismatchDescription) {
                final boolean matches = modifier.existsOn(item);
                if (!matches) {
                    mismatchDescription.appendValue(item).appendText(" did not have modifier ").appendValue(modifier);
                }
                return matches;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("is a member with modifier " + modifier);
            }
        };
    }

    private static Matcher<Constructor<?>> aConstructorWhoseParameterTypes(final Matcher<List<Class<?>>> parameterMatcher) {
        return new FeatureMatcher<Constructor<?>, List<Class<?>>>(
                parameterMatcher, "a constructor whose parameter types", "'s parameter types") {
            @Override
            protected List<Class<?>> featureValueOf(final Constructor<?> actual) {
                return asList(actual.getParameterTypes());
            }
        };
    }

    private static Matcher<Constructor<?>> aConstructorWhoseThrownException(final Matcher<? extends Throwable> throwableMatcher) {
        return new FeatureMatcher<Constructor<?>, Throwable>(
                throwableMatcher, "a constructor whose thrown exception", "'s thrown exception") {
            @Override
            protected Throwable featureValueOf(final Constructor<?> constructor) {
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

    private static Matcher<Throwable> aThrowableWhoseMessage(final Matcher<String> messageMatcher) {
        return new FeatureMatcher<Throwable, String>(messageMatcher, "a throwable whose message", "'s message") {
            @Override
            protected String featureValueOf(final Throwable actual) {
                return actual.getMessage();
            }
        };
    }

    private Assert() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
