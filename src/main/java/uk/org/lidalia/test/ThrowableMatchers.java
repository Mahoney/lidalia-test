package uk.org.lidalia.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ThrowableMatchers {

    public static <ThrowableType extends Throwable> Matcher<Runnable> doesThrow(final ThrowableType expectedThrowable) {
        return new BaseMatcher<Runnable>() {
            @Override
            public boolean matches(Object o) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a runnable that throws " + expectedThrowable);
            }
        };
    }
}
