package uk.org.lidalia.test;

import org.joda.time.Instant;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.org.lidalia.test.StaticTimeRule.alwaysNow;

public class StaticTimeRuleTests {

    @Rule public StaticTimeRule nowStaticTime = alwaysNow();

    @Test
    public void nowStaticTimeKeepsTimeStatic() {
        assertThat(new Instant(), is(nowStaticTime.getInstant()));
    }
}
