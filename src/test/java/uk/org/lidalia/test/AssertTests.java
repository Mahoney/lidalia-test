package uk.org.lidalia.test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.aListWhoseElementAtIndex;
import static uk.org.lidalia.test.Assert.isAMemberWithModifier;
import static uk.org.lidalia.test.Assert.aClassWhoseSuperClass;
import static uk.org.lidalia.test.Assert.isNotInstantiable;
import static uk.org.lidalia.test.Assert.aCollectionWhoseLength;
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
        assertThat(method, isAMemberWithModifier(FINAL));
    }

    @Test public void hasModifierFinalWithNonFinalMethod() {
        AssertionError error = shouldThrow(AssertionError.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Method method = DummyClass.class.getMethod("nonFinalMethod");
                assertThat(method, isAMemberWithModifier(FINAL));
                return null;
            }
        });
        assertThat(error.getMessage(), is("\n" +
                "Expected: is a member with modifier FINAL\n" +
                "     but: <public void uk.org.lidalia.test.AssertTests$DummyClass.nonFinalMethod()> " +
                "did not have modifier <FINAL>"));
    }

    @Test public void lengthWithMatchingLength() {
        assertThat(asList("foo", "bar"), aCollectionWhoseLength(is(2)));
    }

    @Test public void lengthWithNonMatchingLength() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(asList("foo", "bar"), is(aCollectionWhoseLength(is(0))));
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: is a Collection whose length is <0>\n" +
                "     but: <[foo, bar]>'s length was <2>"));
    }

    @Test public void atIndexCorrectExpectation() {
        final List<String> strings = asList("foo", "bar");
        assertThat(strings, aListWhoseElementAtIndex(0, is("foo")));
        assertThat(strings, aListWhoseElementAtIndex(1, is("bar")));
    }

    @Test public void atIndexWrongExpectation() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(asList("foo", "bar"), aListWhoseElementAtIndex(1, is("not bar")));
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: a List whose element at index 1 is \"not bar\"\n" +
                "     but: <[foo, bar]>'s element at index 1 was \"bar\""));
    }

    @Test public void atIndexWithIndexOutOfBounds() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(asList("foo", "bar"), aListWhoseElementAtIndex(2, is("something")));
            }
        });
        assertThat(expected.getMessage(), is("[foo, bar] has no element at index 2"));
    }

    @Test public void aClassWhoseSuperClassMatching() {
        assertThat(Integer.class, is(aClassWhoseSuperClass(is(sameInstance(Number.class)))));
    }

    @Test public void aClassWhoseSuperClassNonMatching() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(Integer.class, is(aClassWhoseSuperClass(is(equalTo(Object.class)))));
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: is a Class whose super class is <class java.lang.Object>\n" +
                "     but: <class java.lang.Integer>'s super class was <class java.lang.Number>"));
    }

    @Test public void assertNotInstantiableWithUninstantiableClass() {
        assertThat(Uninstantiable.class, isNotInstantiable());
    }

    @Test public void assertNotInstantiableWithInstantiableClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(Instantiable.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$Instantiable>'s constructors <[private uk.org.lidalia.test.AssertTests$Instantiable()]>'s element at index 0 <private uk.org.lidalia.test.AssertTests$Instantiable()>'s thrown exception was null"));
    }

    @Test public void assertNotInstantiableWithWrongExceptionTypeClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(WrongExceptionType.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$WrongExceptionType>'s constructors <[private uk.org.lidalia.test.AssertTests$WrongExceptionType()]>'s element at index 0 <private uk.org.lidalia.test.AssertTests$WrongExceptionType()>'s thrown exception <java.lang.IllegalArgumentException: Not instantiable> is a java.lang.IllegalArgumentException"));
    }

    @Test public void assertNotInstantiableWithWrongExceptionMessageClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(WrongExceptionMessage.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$WrongExceptionMessage>'s constructors <[private uk.org.lidalia.test.AssertTests$WrongExceptionMessage()]>'s element at index 0 <private uk.org.lidalia.test.AssertTests$WrongExceptionMessage()>'s thrown exception <java.lang.UnsupportedOperationException: other message>'s message was \"other message\""));
    }

    @Test public void assertNotInstantiableWithPublicConstructorClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(PublicConstructor.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$PublicConstructor>'s constructors <[public uk.org.lidalia.test.AssertTests$PublicConstructor()]>'s element at index 0 <public uk.org.lidalia.test.AssertTests$PublicConstructor()> did not have modifier <PRIVATE>"));
    }

    @Test public void assertNotInstantiableWithWrongSuperClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(WrongSuperClass.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$WrongSuperClass>'s super class was <class java.util.Date>"));
    }

    @Test public void assertNotInstantiableWithConstructorWithParamClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(ConstructorWithParam.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$ConstructorWithParam>'s constructors <[private uk.org.lidalia.test.AssertTests$ConstructorWithParam(java.lang.String)]>'s element at index 0 <private uk.org.lidalia.test.AssertTests$ConstructorWithParam(java.lang.String)>'s parameter types <[class java.lang.String]>'s length was <1>"));
    }

    @Test public void assertNotInstantiableWithMultipleConstructorsClass() {
        AssertionError expected = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(MultipleConstructors.class, isNotInstantiable());
            }
        });
        assertThat(expected.getMessage(), is("\n" +
                "Expected: (a Class whose super class is <class java.lang.Object> and a Class whose set of constructors (is a Collection whose length is <1> and is a List whose element at index 0 ((is a constructor whose parameter types is a Collection whose length is <0> and is a member with modifier PRIVATE) and a constructor whose thrown exception (is an instance of java.lang.UnsupportedOperationException and is a throwable whose message is \"Not instantiable\"))))\n" +
                "     but: <class uk.org.lidalia.test.AssertTests$MultipleConstructors>'s constructors <[private uk.org.lidalia.test.AssertTests$MultipleConstructors(), private uk.org.lidalia.test.AssertTests$MultipleConstructors(java.lang.String)]>'s length was <2>"));
    }

    private static class Uninstantiable {
        private Uninstantiable() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class PublicConstructor {
        public PublicConstructor() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class WrongExceptionType {
        private WrongExceptionType() {
            throw new IllegalArgumentException("Not instantiable");
        }
    }

    private static class WrongExceptionMessage {
        private WrongExceptionMessage() {
            throw new UnsupportedOperationException("other message");
        }
    }

    private static class WrongSuperClass extends Date {
        private WrongSuperClass() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class ConstructorWithParam {
        private ConstructorWithParam(String param) {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class MultipleConstructors {
        private MultipleConstructors() {
            throw new UnsupportedOperationException("Not instantiable");
        }

        private MultipleConstructors(String param) {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    private static class Instantiable {
    }

    @Test public void notInstantiable() {
        assertThat(Assert.class, isNotInstantiable());
    }
}
