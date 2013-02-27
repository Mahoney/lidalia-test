package uk.org.lidalia.test;

import java.io.PrintStream;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class SystemOutputRuleUnitTests {

    @Test
    public void replacesAndRestoresSystemOut() throws Throwable {
        PrintStream originalPrintStream = System.out;
        SystemOutputRule rule = new SystemOutputRule();
        rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.print("some text");
            }
        }, Description.EMPTY).evaluate();

        assertThat(rule.getSystemOut(), is("some text"));
        assertThat(System.out, is(sameInstance(originalPrintStream)));
    }

    @Test
    public void restoresSystemOutAfterException() throws Throwable {
        PrintStream originalPrintStream = System.out;
        final SystemOutputRule rule = new SystemOutputRule();

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


        assertThat(System.out, is(sameInstance(originalPrintStream)));
    }

    @Test
    public void replacesAndRestoresSystemErr() throws Throwable {
        PrintStream originalPrintStream = System.err;
        SystemOutputRule rule = new SystemOutputRule();
        rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.err.print("some text");
            }
        }, Description.EMPTY).evaluate();

        assertThat(rule.getSystemErr(), is("some text"));
        assertThat(System.err, is(sameInstance(originalPrintStream)));
    }

    @Test
    public void restoresSystemErrAfterException() throws Throwable {
        PrintStream originalPrintStream = System.err;
        final SystemOutputRule rule = new SystemOutputRule();

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


        assertThat(System.err, is(sameInstance(originalPrintStream)));
    }
}
