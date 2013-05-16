## Lidalia Test

Some utility Hamcrest matchers, a means to test exception behaviour more robustly than
[JUnit](https://github.com/junit-team/junit/wiki) provides by default and JUnit rules for common testing scenarios.

See the [JavaDocs](./apidocs/index.html) for full documentation and the [Test Source](./xref-test/index.html) for complete
examples of usage. Below are some examples from some of the more common classes.

### Examples

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
    import static uk.org.lidalia.test.Assert.isNotInstantiable;

    public class MyStaticMethodsTest {
        @Test
        public void notInstantiable() {
            assertThat(MyStaticMethods.class, isNotInstantiable());
        }
    }

####
