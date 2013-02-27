package uk.org.lidalia.test;

import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;
import static uk.org.lidalia.test.StaticTimeRule.always;
import static uk.org.lidalia.test.StaticTimeRule.alwaysNow;
import static uk.org.lidalia.test.StaticTimeRule.alwaysStartOfEpoch;

public class StaticTimeRuleUnitTests {

    @Test
    public void alwaysStartOfEpochSetsToStartOfEpoch() throws Throwable {
        final StaticTimeRule rule = alwaysStartOfEpoch();
        rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final Instant epochStart = new Instant(0L);
                assertThat(new Instant(), is(epochStart));
            }
        }, Description.EMPTY).evaluate();
    }

    @Test
    public void alwaysSetsToGivenTime() throws Throwable {
        final Instant givenTime = new Instant(1234L);
        final StaticTimeRule rule = always(givenTime);
        rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                assertThat(new Instant(), is(givenTime));
            }
        }, Description.EMPTY).evaluate();
    }

    @Test
    public void replacesAndRestoresSystemTime() throws Throwable {
        final StaticTimeRule rule = alwaysNow();
        rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(MILLISECONDS.toMillis(1));
                assertThat(new Instant(), is(rule.getInstant()));
            }
        }, Description.EMPTY).evaluate();

        Thread.sleep(MILLISECONDS.toMillis(1));
        assertThat(new Instant(), is(greaterThan(rule.getInstant())));
    }

    @Test
    public void restoresSystemTimeAfterException() throws Throwable {
        final StaticTimeRule rule = alwaysNow();

        shouldThrow(Exception.class, new Runnable() {
            @Override
            public void run() {
                try {
                    rule.apply(new Statement() {
                        @Override
                        public void evaluate() throws Throwable {
                            throw new Exception();
                        }
                    }, Description.EMPTY).evaluate();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        });

        Thread.sleep(MILLISECONDS.toMillis(1));
        assertThat(new Instant(), is(greaterThan(rule.getInstant())));
    }
}
