package uk.org.lidalia.test;

import com.google.common.base.Optional;

import static uk.org.lidalia.lang.Exceptions.throwUnchecked;

/**
 * Static functions for returning an expected type of Throwable from a block of code for unit testing purposes.
 */
public final class ShouldThrow {

    private static final String INVOKER_INVOCATION_EXCEPTION_CLASSNAME = "org.codehaus.groovy.runtime.InvokerInvocationException";

    /**
     * Asserts that a code block throws a {@link Throwable} of the given type and returns it to permit further assertions on the
     * Throwable instance returned.
     *
     * @param expectedThrowableType the class of the throwable the code block should throw
     * @param workThatShouldThrowThrowable a code block that ought to throw the expected type when evaluated
     * @param <ThrowableType> the type of the throwable the code block should throw
     * @throws AssertionError if no Throwable is thrown at all whilst executing the code block
     * @throws Throwable any other Throwable thrown by the code block that is not an instance of the expected type
     * @return the instance of the expected throwable type thrown by the code block, if it was thrown
     */
    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Runnable workThatShouldThrowThrowable) {
        return shouldThrow(expectedThrowableType, Optional.<String>absent(), workThatShouldThrowThrowable);
    }

    /**
     * Asserts that a code block throws a {@link Throwable} of the given type and returns it to permit further assertions on the
     * Throwable instance returned.
     *
     * @param expectedThrowableType the class of the throwable the code block should throw
     * @param message the message to be used as the message of the AssertionError if no throwable is thrown at all
     * @param workThatShouldThrowThrowable a code block that ought to throw the expected type when evaluated
     * @param <ThrowableType> the type of the throwable the code block should throw
     * @throws AssertionError if no Throwable is thrown at all whilst executing the code block
     * @throws Throwable any other Throwable thrown by the code block that is not an instance of the expected type
     * @return the instance of the expected throwable type thrown by the code block, if it was thrown
     */
    public static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final String message,
            final Runnable workThatShouldThrowThrowable) {
        return shouldThrow(expectedThrowableType, Optional.of(message), workThatShouldThrowThrowable);
    }

    @SuppressWarnings({ "unchecked", "PMD.AvoidCatchingThrowable" })
    private static <ThrowableType extends Throwable> ThrowableType shouldThrow(
            final Class<ThrowableType> expectedThrowableType,
            final Optional<String> message,
            final Runnable workThatShouldThrowThrowable) {
        try {
            workThatShouldThrowThrowable.run();
            throw new AssertionError(message.or("No exception thrown"));
        } catch (final Throwable actualThrowableThrown) {
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
