package uk.org.lidalia.test;

import org.hamcrest.Matcher;
import org.junit.Test;

import static java.lang.System.lineSeparator;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.org.lidalia.test.Assert.aClassWhoseSuperClass;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

public class FeatureMatcherTests {
    
    @Test
    public void nonMatchingFeature() {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(String.class, is(aClassWhoseName(is("List"))));
            }
        });
        assertThat(assertionError.getMessage(), is(lineSeparator() +
                "Expected: is a Class whose name is \"List\""+ lineSeparator()+
                "     but: <class java.lang.String>'s name was \"java.lang.String\""));
    }

    @Test
    public void nonMatchingNestedFeature() {
        AssertionError assertionError = shouldThrow(AssertionError.class, new Runnable() {
            @Override
            public void run() {
                assertThat(String.class, is(aClassWhoseSuperClass(is(aClassWhoseName(is("List"))))));
            }
        });
        assertThat(assertionError.getMessage(), is(lineSeparator() +
                "Expected: is a Class whose super class is a Class whose name is \"List\""+ lineSeparator()+
                "     but: <class java.lang.String>'s super class <class java.lang.Object>'s name was \"java.lang.Object\""));
    }

    private static FeatureMatcher<Class<?>, String> aClassWhoseName(final Matcher<String> classMatcher) {
        return new FeatureMatcher<Class<?>, String>(classMatcher, "a Class whose name", "'s name") {
            @Override
            protected String featureValueOf(Class<?> actual) {
                return actual.getName();
            }
        };
    }
}
