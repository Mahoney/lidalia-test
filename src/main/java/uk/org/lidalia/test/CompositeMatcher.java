package uk.org.lidalia.test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CompositeMatcher<T> extends BaseMatcher<T> {

    private final Iterable<MatcherFactory<? extends T>> matcherFactories;
    private final List<Matcher<? extends T>> matchers = new ArrayList<Matcher<? extends T>>();

    public CompositeMatcher(MatcherFactory<? extends T> matcherCreator, MatcherFactory<? extends T>... matcherFactories) {
        this.matcherFactories = Lists.asList(matcherCreator, matcherFactories);
    }

    @Override
    public boolean matches(final Object toMatchAgainst) {
        for (MatcherFactory<? extends T> matcherFactory : matcherFactories) {
            matchers.add(matcherFactory.make(toMatchAgainst));
        }
        return Iterables.all(matchers, new Predicate<Matcher<? extends T>>() {
            @Override
            public boolean apply(@Nullable Matcher<? extends T> matcher) {
                return matcher.matches(toMatchAgainst);
            }
        });
    }

    @Override
    public void describeTo(Description description) {
        description.appendList("(", " and ", ")", matchers);
    }

    public static abstract class MatcherFactory<T> {
        public abstract Matcher<T> make(Object toMatchAgainst);
    }
}
