package uk.org.lidalia.test;

import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class StaticTimeRule implements TestRule {

    public static StaticTimeRule alwaysStartOfEpoch() {
        final Instant startOfEpoch = new Instant(0L);
        return always(startOfEpoch);
    }

    public static StaticTimeRule alwaysNow() {
        final Instant now = new Instant();
        return always(now);
    }

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
