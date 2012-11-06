package uk.org.lidalia.test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.atIndex;
import static uk.org.lidalia.test.Assert.hasModifier;
import static uk.org.lidalia.test.Assert.hasSuperClassThat;
import static uk.org.lidalia.test.Assert.isNotInstantiable;
import static uk.org.lidalia.test.Assert.length;
import static uk.org.lidalia.test.Modifier.FINAL;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class AssertTests {

    @SuppressWarnings("unused")
    private static final class DummyClass {
        public final void finalMethod() {}
        public void nonFinalMethod() {}
        protected final void protectedFinalMethod() {}
        public final void finalMethod(String arg1, Integer arg2) {}
    }

    @Test public void hasModifierFinalWithFinalMethod() throws Exception {
        Method method = DummyClass.class.getMethod("finalMethod");
        assertThat(method, hasModifier(FINAL));
    }

    @Test public void hasModifierFinalWithNonFinalMethod() {
        AssertionError error = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Method method = DummyClass.class.getMethod("nonFinalMethod");
                assertThat(method, hasModifier(FINAL));
                return null;
            }
        });
        assertThat(error.getMessage(), is("\n" +
                "Expected: should have modifier FINAL\n" +
                "     but: <public void uk.org.lidalia.test.AssertTests$DummyClass.nonFinalMethod()> " +
                "did not have modifier <FINAL>"));
    }

    @Test public void lengthWithMatchingLength() {
        assertThat(asList("foo", "bar"), length(is(2)));
    }

    @Test public void lengthWithNonMatchingLength() {
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
        assertThat(expected.getMessage(), is("\n" +
                "Expected: a Collection of length that is <0>\n" +
                "     but: length of [foo, bar] was <2>"));
    }

    @Test public void atIndexCorrectExpectation() {
        final List<String> strings = asList("foo", "bar");
        assertThat(strings, atIndex(0, is("foo")));
        assertThat(strings, atIndex(1, is("bar")));
    }

    @Test public void atIndexWrongExpectation() {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertThat(asList("foo", "bar"), atIndex(1, is("not bar")));
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: a List with element at index 1 that is \"not bar\"\n" +
                "     but: the element at index 1 of [foo, bar] was \"bar\""));
    }

    @Test public void atIndexWithIndexOutOfBounds() {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertThat(asList("foo", "bar"), atIndex(2, is("something")));
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertThat(expected.getMessage(), is("[foo, bar] has no element at index 2"));
    }

    @Test public void hasSuperClassMatching() {
        assertThat(Integer.class, hasSuperClassThat(CoreMatchers.<Class<?>>equalTo(Number.class)));
    }

    @Test public void hasSuperClassNonMatching() {
        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    assertThat(Integer.class, hasSuperClassThat(CoreMatchers.<Class<?>>is(Object.class)));
                    return null;
                } catch (Throwable throwable) {
                    throw (AssertionError) throwable;
                }
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: a Class whose immediate super class is <class java.lang.Object>\n" +
                "     but: immediate super class of class java.lang.Integer was <class java.lang.Number>"));
    }

    @Test public void assertNotInstantiableWithUninstantiableClass() {
        assertThat(Uninstantiable.class, isNotInstantiable());
    }

    private static class Uninstantiable {
        private Uninstantiable() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    @Test public void assertNotInstantiableWithInstantiableClass() {
//        AssertionError expected = shouldThrow(AssertionError.class, new Callable<Void>() {
//            @Override
//            public Void call() throws Exception {
//                try {
                    assertThat(Instantiable.class, isNotInstantiable());
//                    return null;
//                } catch (Throwable throwable) {
//                    throw (AssertionError) throwable;
//                }
//            }
//        });
//        assertThat(expected.getMessage(), is("\n" +
//                "Expected: (a Class whose immediate super class is <class java.lang.Object> and a Class with constructors that are (a Collection of length that is <1> and a List with element at index 0 that ((has parameter types that are a Collection of length that is <0> and should have modifier PRIVATE) and  (is an instance of java.lang.UnsupportedOperationException and  is \"Not instantiable\"))))\n" +
//                "     but: a Class with constructors that are (a Collection of length that is <1> and a List with element at index 0 that ((has parameter types that are a Collection of length that is <0> and should have modifier PRIVATE) and  (is an instance of java.lang.UnsupportedOperationException and  is \"Not instantiable\"))) the constructors of class uk.org.lidalia.test.AssertTests$Instantiable a List with element at index 0 that ((has parameter types that are a Collection of length that is <0> and should have modifier PRIVATE) and  (is an instance of java.lang.UnsupportedOperationException and  is \"Not instantiable\")) the element at index 0 of [private uk.org.lidalia.test.AssertTests$Instantiable()]  (is an instance of java.lang.UnsupportedOperationException and  is \"Not instantiable\")  of private uk.org.lidalia.test.AssertTests$Instantiable() is an instance of java.lang.UnsupportedOperationException <java.lang.RuntimeException> is a java.lang.RuntimeException"));
    }

    private static class Instantiable {
        private Instantiable() {
            throw new RuntimeException();
        }
    }

    @Test public void notInstantiable() {
        assertThat(Assert.class, isNotInstantiable());
    }
}
