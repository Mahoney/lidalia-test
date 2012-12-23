package uk.org.lidalia.test;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;

class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Matcher<? super T> matcher;

    CombinableMatcher(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatch) {
        if (!matcher.matches(item)) {
            matcher.describeMismatch(item, mismatch);
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(matcher);
    }

    CombinableMatcher<T> and(Matcher<? super T> other) {
        return new CombinableMatcher<T>(new AllOf<T>(templatedListWith(other)));
    }

    private ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
        ArrayList<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(matcher);
        matchers.add(other);
        return matchers;
    }

    /**
     * Creates a matcher that matches when both of the specified matchers match the examined object.
     * <p/>
     * For example:
     * <pre>assertThat("fab", both(containsString("a")).and(containsString("b")))</pre>
     */
    @Factory
    static <LHS> CombinableMatcher<LHS> both(Matcher<? super LHS> matcher) {
        return new CombinableMatcher<LHS>(matcher);
    }
}
