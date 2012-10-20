package uk.org.lidalia.test;

import java.util.HashSet;
import java.util.concurrent.Callable;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.assertFinal;
import static uk.org.lidalia.test.Assert.assertNotInstantiable;
import static uk.org.lidalia.test.Assert.isNotInstantiable;
import static uk.org.lidalia.test.Assert.length;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class AssertTests {

    @SuppressWarnings("unused")
    private static final class DummyClass {
        public final void finalMethod() {}
        public void nonFinalMethod() {}
        protected final void protectedFinalMethod() {}
        public final void finalMethod(String arg1, Integer arg2) {}
    }

    @Test public void assertFinalWithFinalMethod() throws Exception {
        assertFinal(DummyClass.class, "finalMethod");
    }

    @Test public void assertFinalWithNonFinalMethod() throws Throwable {
        AssertionError error = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                assertFinal(DummyClass.class, "nonFinalMethod");
                return null;
            }
        });
        assertEquals("nonFinalMethod is not final", error.getMessage());
    }

    @Test(expected = NoSuchMethodException.class)
    public void assertFinalWithNonPublicMethod() throws Exception {
        assertFinal(DummyClass.class, "protectedFinalMethod");
    }

    @Test public void assertFinalWithMatchingArgs() throws Exception {
        assertFinal(DummyClass.class, "finalMethod", String.class, Integer.class);
    }

    @Test(expected = NoSuchMethodException.class)
    public void assertFinalWithNonMatchingArgs() throws Exception {
        assertFinal(DummyClass.class, "finalMethod", String.class, String.class);
    }

    @Test public void lengthWithMatchingLength() {
        assertThat(asList("foo", "bar"), length(is(2)));
    }

    @Test public void lengthWithNonMatchingLength() throws Throwable {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertThat(new HashSet<String>(asList("foo", "bar")), length(is(0)));
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertEquals("\n" +
                "Expected: a Collection of length that is <0>\n" +
                "     but: length of [foo, bar] was <2>", expected.getMessage());
    }

    @Test public void assertNotInstantiableWithUninstantiableClass() throws Throwable {
        assertThat(Uninstantiable.class, isNotInstantiable());
    }

    private static class Uninstantiable {
        private Uninstantiable() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    @Test public void assertNotInstantiableWithInstantiableClass() throws Throwable {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertThat(Instantiable.class, isNotInstantiable());
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertEquals("Able to instantiate class uk.org.lidalia.test.AssertTests$Instantiable", expected.getMessage());
    }

    private static class Instantiable {
        private Instantiable() {

        }
    }

    @Test public void notInstantiable() throws Throwable {
        assertThat(Assert.class, isNotInstantiable());
    }
}
