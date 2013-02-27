package uk.org.lidalia.test;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SystemOutputRuleTests {

    @Rule public SystemOutputRule systemOutputRule = new SystemOutputRule();

    @Test
    public void useSysOutOnce() {
        System.out.print("some text");
        assertThat(systemOutputRule.getSystemOut(), is("some text"));
    }

    @Test
    public void useSysOutAgain() {
        System.out.print("some other text");
        assertThat(systemOutputRule.getSystemOut(), is("some other text"));
    }

    @Test
    public void useSysErrOnce() {
        System.err.print("some text");
        assertThat(systemOutputRule.getSystemErr(), is("some text"));
    }

    @Test
    public void useSysErrAgain() {
        System.err.print("some other text");
        assertThat(systemOutputRule.getSystemErr(), is("some other text"));
    }
}
