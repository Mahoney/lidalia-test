package uk.org.lidalia.test;

import com.google.common.base.Optional;

import static uk.org.lidalia.lang.Exceptions.throwUnchecked;

public final class ShouldThrow {

    private static final String INVOKER_INVOCATION_EXCEPTION_CLASSNAME = "org.codehaus.groovy.runtime.InvokerInvocationException";

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Runnable workThatShouldThrowThrowable) {
        return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final String message,
            final Runnable workThatShouldThrowThrowable) {
        return shouldThrow(expectedThrowableType, Optional.of(message), workThatShouldThrowThrowable);
    }

    @SuppressWarnings("unchecked")
    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Optional<String> message,
            final Runnable workThatShouldThrowThrowable) {
        try {
            workThatShouldThrowThrowable.run();
            throw new AssertionError(message.or("No exception thrown"));
        } catch (final Throwable actualThrowableThrown) { // NOPMD Throwable is thrown if it was not expected
            final Throwable trueThrowable = extractTrueThrowable(expectedThrowableType, actualThrowableThrown);
            if (!expectedThrowableType.isInstance(trueThrowable)) {
                throwUnchecked(actualThrowableThrown);
            }
            return (ThrowableType) trueThrowable;
        }
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

    private ShouldThrow() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
