## Lidalia Test

Some utility Hamcrest matchers, a means to test exception behaviour more robustly than
[JUnit](https://github.com/junit-team/junit/wiki) provides by default and JUnit rules for common testing scenarios.

See the [JavaDocs](./apidocs/index.html) for full documentation and the [Test Source](./xref-test/index.html) for complete
examples of usage. Below are some examples from some of the more common classes.

Details on how to depend on this library in your favourite build tool can be found [here](./dependency-info.html).

### Examples

#### Testing a time sensitive bit of code

    import org.joda.time.Instant;
    import org.junit.Rule;
    import org.junit.Test;
    import uk.org.lidalia.test.StaticTimeRule;

    import static org.hamcrest.Matchers.is;
    import static org.junit.Assert.assertThat;
    import static uk.org.lidalia.test.StaticTimeRule.alwaysNow;

    public class StaticTimeTest {

        @Rule public StaticTimeRule nowStaticTime = alwaysNow();

        @Test
        public void nowStaticTimeKeepsTimeStatic() {
            assertThat(new Instant(), is(nowStaticTime.getInstant()));
        }
    }

#### Testing code that writes to System.out

    import org.junit.Rule;
    import org.junit.Test;
    import uk.org.lidalia.test.SystemOutputRule;

    import static org.hamcrest.Matchers.is;
    import static org.junit.Assert.assertThat;

    public class SystemOutputTest {

        @Rule public SystemOutputRule systemOutputRule = new SystemOutputRule();

        @Test
        public void useSysOut() {
            System.out.print("some text");
            assertThat(systemOutputRule.getSystemOut(), is("some text"));
        }
    }

#### Asserting details about a thrown exception

    import org.junit.Test;

    import uk.org.lidalia.lang.Task;

    import static org.hamcrest.Matchers.is;
    import static org.junit.Assert.assertThat;
    import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

    public class ShouldThrowTest {

        @Test
        public void throwsExpectedException() {
            RuntimeException exceptionThrown = shouldThrow(RuntimeException.class, new Task() {
                @Override
                public void perform() {
                    final Exception cause = new Exception("cause exception message");
                    throw new RuntimeException("exception message", cause);
                }
            });
            assertThat(exceptionThrown.getMessage(), is("exception message"));
            assertThat(exceptionThrown.getCause().getMessage(), is("cause exception message"));
        }
    }


#### Checking a static utility method class is not instantiable

The primary value in this is to keep code coverage tools happy
[which may not be such a great aim anyway](http://adiws.blogspot.co.uk/2012/04/code-coverage-considered-harmful.html). Still, if
someone has foisted them on you this allows you to test this edge case with a genuine assertion and no further pain.

    public class MyStaticMethods {
        ... lots of static methods ...

        private MyStaticMethods() {
            throw new UnsupportedOperationException("Not instantiable");
        }
    }

    import org.junit.Test;
    import static org.junit.Assert.assertThat;
    import static uk.org.lidalia.test.Assert.isNotInstantiable;

    public class MyStaticMethodsTest {
        @Test
        public void notInstantiable() {
            assertThat(MyStaticMethods.class, isNotInstantiable());
        }
    }
