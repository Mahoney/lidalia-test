package uk.org.lidalia.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.shouldThrow;

public class ThrowableMatchers {

    public static <ThrowableType extends Throwable> Matcher<Callable<Void>> doesThrow(final ThrowableType expectedThrowable) {
        return new BaseMatcher<Callable<Void>>() {
            @Override
            public boolean matches(Object o) {
                Callable<Void> workThatShouldThrowThrowable = (Callable<Void>) o;
                final ThrowableType actualThrowable = shouldThrow(ThrowableMatchers.getClass(expectedThrowable), workThatShouldThrowThrowable);
                assertThat(actualThrowable, is(sameInstance(expectedThrowable)));
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a runnable that throws " + expectedThrowable);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <CompileTimeType> Class<? extends CompileTimeType> getClass(final CompileTimeType object) {
        return (Class<? extends CompileTimeType>) object.getClass();
    }
}
