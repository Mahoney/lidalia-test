package uk.org.lidalia.test;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.ThrowableMatchers.doesThrow;

public class ThrowableMatchersTest {

    @Test
    public void throwsSameThrowable() {
        final RuntimeException throwable = new RuntimeException();
        assertThat(new Runnable() {
            @Override
            public void run() {
                throw throwable;
            }
        }, doesThrow(throwable));
    }
}
