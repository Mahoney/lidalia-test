package uk.org.lidalia.test;

import java.util.concurrent.Callable;

import com.google.common.base.Optional;

import static org.junit.Assert.assertSame;

public final class ShouldThrow {

    private static final String INVOKER_INVOCATION_EXCEPTION_CLASSNAME = "org.codehaus.groovy.runtime.InvokerInvocationException";

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final String message,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, Optional.of(message), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final String message,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        return shouldThrow(expectedThrowableType, Optional.of(message), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final String message,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.of(message), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final String message,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, Optional.of(message), workThatShouldThrowThrowable);
    }

    private static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final Optional<String> message,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        shouldThrow(expectedThrowable, message, toCallable(workThatShouldThrowThrowable));
    }

    private static <ThrowableType extends Throwable> void shouldThrow(
            final ThrowableType expectedThrowable,
            final Optional<String> message,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        final ThrowableType actualThrowable = shouldThrow(getClass(expectedThrowable), message, workThatShouldThrowThrowable);
        assertSame(message.or("Did not throw correct Throwable;"), expectedThrowable, actualThrowable);
    }

    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Optional<String> message,
            final Runnable workThatShouldThrowThrowable) throws Throwable {
        final Callable<Void> workAsCallable = toCallable(workThatShouldThrowThrowable);
        return shouldThrow(expectedThrowableType, message, workAsCallable);
    }

    private static Callable<Void> toCallable(final Runnable workThatShouldThrowThrowable) {
        return new Callable<Void>() {
            @Override
            public Void call() {
                workThatShouldThrowThrowable.run();
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Optional<String> message,
            final Callable<Void> workThatShouldThrowThrowable) throws Throwable {
        try {
            workThatShouldThrowThrowable.call();
        } catch (final Throwable actualThrowableThrown) { // NOPMD Throwable is thrown if it was not expected
            final Throwable trueThrowable = extractTrueThrowable(expectedThrowableType, actualThrowableThrown);
            if (instanceOf(trueThrowable, expectedThrowableType)) {
                return (ThrowableType) trueThrowable;
            }
            throw actualThrowableThrown;
        }
        throw new AssertionError(message.or("No exception thrown"));
    }

    private static <ThrowableType extends Throwable> Throwable extractTrueThrowable(
            final Class<ThrowableType> expectedThrowableType,
            final Throwable actualThrowableThrown) {
        final Throwable trueThrowable;
        if (actualThrowableThrown.getClass().getName().equals(INVOKER_INVOCATION_EXCEPTION_CLASSNAME)
                && !expectedThrowableType.getName().equals(INVOKER_INVOCATION_EXCEPTION_CLASSNAME)) {
            trueThrowable = actualThrowableThrown.getCause();
        } else {
            trueThrowable = actualThrowableThrown;
        }
        return trueThrowable;
    }

    private static boolean instanceOf(final Object object, final Class<?> aClass) {
        return aClass.isAssignableFrom(object.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <CompileTimeType> Class<? extends CompileTimeType> getClass(final CompileTimeType object) {
        return (Class<? extends CompileTimeType>) object.getClass();
    }

    private ShouldThrow() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
