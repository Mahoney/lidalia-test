package uk.org.lidalia.test;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.shouldThrow;
import static uk.org.lidalia.test.UninstantiableMatchers.isNotInstantiable;

public class MatchersTests {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    public void isNotInstantiableWithUninstantiableClass() {
        assertThat(Uninstantiable.class, isNotInstantiable());
    }

    @Test
    public void isNotInstantiableWithClassWithTwoConstructors() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(TwoConstructors.class, isNotInstantiable());
            }
        });
        assertEquals(LINE_SEPARATOR + "Expected: (class with 1 private no args constructor and class extending java.lang.Object and class that throws UnsupportedOperationException when no args constructor is invoked)" + LINE_SEPARATOR +
                "     got: <" + TwoConstructors.class + ">" + LINE_SEPARATOR,
                assertionError.getMessage());
    }

    @Test
    public void isNotInstantiableWithClassWithSuperclass() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ExtendsOtherThanObject.class, isNotInstantiable());
            }
        });
        assertEquals(LINE_SEPARATOR + "Expected: (class with 1 private no args constructor and class extending java.lang.Object and class that throws UnsupportedOperationException when no args constructor is invoked)" + LINE_SEPARATOR +
                "     got: <" + ExtendsOtherThanObject.class + ">" + LINE_SEPARATOR,
                assertionError.getMessage());
    }

    @Test
    public void isNotInstantiableWithClassWithConstructorWithArgs() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ConstructorWithArg.class, isNotInstantiable());
            }
        });
        assertEquals(LINE_SEPARATOR + "Expected: (class with 1 private no args constructor and class extending java.lang.Object and class that throws UnsupportedOperationException when no args constructor is invoked)" + LINE_SEPARATOR +
                "     got: <" + ConstructorWithArg.class + ">" + LINE_SEPARATOR,
                assertionError.getMessage());
    }

    @Test
    public void isNotInstantiableWithClassWithPublicConstructor() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(PublicConstructor.class, isNotInstantiable());
            }
        });
        assertEquals(LINE_SEPARATOR + "Expected: (class with 1 private no args constructor and class extending java.lang.Object and class that throws UnsupportedOperationException when no args constructor is invoked)" + LINE_SEPARATOR +
                "     got: <" + PublicConstructor.class + ">" + LINE_SEPARATOR,
                assertionError.getMessage());
    }

    @Test
    public void isNotInstantiableWithClassWithReflectivelyCallableConstructor() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ReflectivelyCallableConstructor.class, isNotInstantiable());
            }
        });
        assertEquals(LINE_SEPARATOR + "Expected: (class with 1 private no args constructor and class extending java.lang.Object and class that throws UnsupportedOperationException when no args constructor is invoked)" + LINE_SEPARATOR +
                "     got: <" + ReflectivelyCallableConstructor.class + ">" + LINE_SEPARATOR,
                assertionError.getMessage());
    }

    private static class Uninstantiable {
        private Uninstantiable() {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class TwoConstructors {
        private TwoConstructors() {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }

        private TwoConstructors(String arg) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class ExtendsOtherThanObject extends HashMap<String, String> {
        private ExtendsOtherThanObject() {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class ConstructorWithArg {
        private ConstructorWithArg(String arg) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class PublicConstructor {
        public PublicConstructor() {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class ReflectivelyCallableConstructor {
        private ReflectivelyCallableConstructor() {
        }
    }
}
