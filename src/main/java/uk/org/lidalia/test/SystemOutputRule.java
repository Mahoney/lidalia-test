package uk.org.lidalia.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A <a href="https://github.com/junit-team/junit/wiki">JUnit</a> rule that facilitates testing code that prints to
 * {@link System#out} or {@link System#err}.
 * <p>
 * At the start of each test the System.out and System.err PrintStreams are replaced with ones that buffer data passed to them in
 * memory, allowing the test to retrieve the contents of the buffer at any time and assert on it. At the end of the test the
 * original PrintStreams are restored allowing access to the console again.
 */
public class SystemOutputRule implements TestRule {

    private final ByteArrayOutputStream sysOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream sysErr = new ByteArrayOutputStream();
    private final PrintStream originalSysOut = System.out;
    private final PrintStream originalSysErr = System.err;

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new DebriefableSystemOutputsStatement(base, sysOut, sysErr, originalSysOut, originalSysErr);
    }

    /**
     * @return the data that has been written to System.out since the test began
     */
    public String getSystemOut() {
        return sysOut.toString();
    }

    /**
     * @return the data that has been written to System.err since the test began
     */
    public String getSystemErr() {
        return sysErr.toString();
    }

    private static class DebriefableSystemOutputsStatement extends Statement {
        private final Statement base;
        private final OutputStream sysOut;
        private final OutputStream sysErr;
        private final PrintStream originalSysOut;
        private final PrintStream originalSysErr;

        public DebriefableSystemOutputsStatement(final Statement base, final OutputStream sysOut, final OutputStream sysErr,
                                                 final PrintStream originalSysOut, final PrintStream originalSysErr) {
            super();
            this.base = base;
            this.sysOut = sysOut;
            this.sysErr = sysErr;
            this.originalSysOut = originalSysOut;
            this.originalSysErr = originalSysErr;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                System.setOut(new PrintStream(sysOut));
                System.setErr(new PrintStream(sysErr));
                base.evaluate();
            } finally {
                System.setOut(originalSysOut);
                System.setErr(originalSysErr);
            }
        }
    }
}
