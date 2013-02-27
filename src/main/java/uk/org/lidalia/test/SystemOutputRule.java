package uk.org.lidalia.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SystemOutputRule implements TestRule {

    private final ByteArrayOutputStream sysOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream sysErr = new ByteArrayOutputStream();
    private final PrintStream originalSysOut = System.out;
    private final PrintStream originalSysErr = System.err;

    @Override
    public Statement apply(final Statement base, Description description) {
        return new DebriefableSystemOutputsStatement(base, sysOut, sysErr, originalSysOut, originalSysErr);
    }

    public String getSystemOut() {
        return sysOut.toString();
    }

    public String getSystemErr() {
        return sysErr.toString();
    }

    private static class DebriefableSystemOutputsStatement extends Statement {
        private final Statement base;
        private final OutputStream sysOut;
        private final OutputStream sysErr;
        private final PrintStream originalSysOut;
        private final PrintStream originalSysErr;

        public DebriefableSystemOutputsStatement(Statement base, OutputStream sysOut, OutputStream sysErr,
                                                 PrintStream originalSysOut, PrintStream originalSysErr) {
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
