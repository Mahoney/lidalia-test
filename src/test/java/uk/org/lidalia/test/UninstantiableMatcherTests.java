package uk.org.lidalia.test;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.shouldThrow;
import static uk.org.lidalia.test.UninstantiableMatchers.isNotInstantiable;

public class UninstantiableMatcherTests {

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
        String expectedErrorMessage = TwoConstructors.class + " has the wrong number of constructors" + LINE_SEPARATOR +
                "Expected: is <1>" + LINE_SEPARATOR +
                "     got: <2>" + LINE_SEPARATOR;
        assertThat(assertionError.getMessage(), is(expectedErrorMessage));
    }

    @Test
    public void isNotInstantiableWithClassWithSuperclass() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ExtendsOtherThanObject.class, isNotInstantiable());
            }
        });

        String expectedErrorMessage = ExtendsOtherThanObject.class + " has an unexpected superclass" + LINE_SEPARATOR +
                "Expected: same(<" + Object.class + ">)" + LINE_SEPARATOR +
                "     got: <" + HashMap.class + ">" + LINE_SEPARATOR;
        assertThat(assertionError.getMessage(), is(expectedErrorMessage));
    }

    @Test
    public void isNotInstantiableWithClassWithConstructorWithArgs() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ConstructorWithArg.class, isNotInstantiable());
            }
        });
        String expectedErrorMessage = ConstructorWithArg.class + " has the wrong number of parameters on constructor" + LINE_SEPARATOR +
                "Expected: is <0>" + LINE_SEPARATOR +
                "     got: <1>" + LINE_SEPARATOR;
        assertThat(assertionError.getMessage(), is(expectedErrorMessage));
    }

    @Test
    public void isNotInstantiableWithClassWithPublicConstructor() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(PublicConstructor.class, isNotInstantiable());
            }
        });
        assertThat(assertionError.getMessage(), is(PublicConstructor.class + "'s constructor should be private"));
    }

    @Test
    public void isNotInstantiableWithClassWithReflectivelyCallableConstructor() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ReflectivelyCallableConstructor.class, isNotInstantiable());
            }
        });
        assertThat(assertionError.getMessage(), is("Able to instantiate " + ReflectivelyCallableConstructor.class + " via reflection"));
    }

    @Test
    public void isNotInstantiableWithClassWithConstructorWithWrongException() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ConstructorThrowsWrongException.class, isNotInstantiable());
            }
        });
        String expectedErrorMessage = ConstructorThrowsWrongException.class + "'s constructor threw the wrong exception" + LINE_SEPARATOR +
                "Expected: is an instance of " + UnsupportedOperationException.class.getName() + LINE_SEPARATOR +
                "     got: <" + RuntimeException.class.getName() + ": " + ConstructorThrowsWrongException.class.getSimpleName() + " is not instantiable>" + LINE_SEPARATOR;;
        assertThat(assertionError.getMessage(), is(expectedErrorMessage));
    }

    @Test
    public void isNotInstantiableWithClassWithConstructorWithWrongExceptionMessage() throws Throwable {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ConstructorThrowsExceptionWithWrongMessage.class, isNotInstantiable());
            }
        });
        String expectedErrorMessage = ConstructorThrowsExceptionWithWrongMessage.class + "'s constructor should throw an exception with a specific message" + LINE_SEPARATOR +
                "Expected: is \"" + ConstructorThrowsExceptionWithWrongMessage.class.getSimpleName() + " is not instantiable\"" + LINE_SEPARATOR +
                "     got: \"Incorrect message\"" + LINE_SEPARATOR;
        assertThat(assertionError.getMessage(), is(expectedErrorMessage));
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

    private static class ConstructorThrowsWrongException {
        private ConstructorThrowsWrongException() {
            throw new RuntimeException(getClass().getSimpleName() + " is not instantiable");
        }
    }

    private static class ConstructorThrowsExceptionWithWrongMessage {
        private ConstructorThrowsExceptionWithWrongMessage() {
            throw new UnsupportedOperationException("Incorrect message");
        }
    }
}
