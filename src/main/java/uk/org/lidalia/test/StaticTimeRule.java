package uk.org.lidalia.test;

import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A <a href="https://github.com/junit-team/junit/wiki">JUnit</a> rule that sets the time returned by the
 * <a href="http://joda-time.sourceforge.net/">Joda Time</a> classes to a static
 * time for the duration of the test.
 * <p>
 * At the start of each test the time as viewed by Joda Time classes is frozen to the provided time. At the end of the test it
 * is returned to the system time. This makes time based testing predictable.
 */
public class StaticTimeRule implements TestRule {

    /**
     * @return a static time rule which will freeze the time at the start of the unix epoch
     */
    public static StaticTimeRule alwaysStartOfEpoch() {
        final Instant startOfEpoch = new Instant(0L);
        return always(startOfEpoch);
    }

    /**
     * @return a static time rule which will freeze the time at the instant this method was invoked
     */
    public static StaticTimeRule alwaysNow() {
        final Instant now = new Instant();
        return always(now);
    }

    /**
     * @param instant the instant at which time will be frozen
     * @return a static time rule which will freeze the time to the provided instant
     */
    public static StaticTimeRule always(final ReadableInstant instant) {
        return new StaticTimeRule(instant);
    }

    private final ReadableInstant instant;

    private StaticTimeRule(final ReadableInstant instant) {
        super();
        this.instant = instant;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new StaticTimeStatement(base, instant);
    }

    /**
     * @return the instant at which time is frozen by this rule
     */
    public ReadableInstant getInstant() {
        return instant;
    }

    private static class StaticTimeStatement extends Statement {
        private final Statement base;
        private final ReadableInstant instant;

        public StaticTimeStatement(final Statement base, final ReadableInstant instant) {
            super();
            this.base = base;
            this.instant = instant;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                DateTimeUtils.setCurrentMillisFixed(instant.getMillis());
                base.evaluate();
            } finally {
                DateTimeUtils.setCurrentMillisSystem();
            }
        }
    }
}
