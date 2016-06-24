package com.hegel.core.test;

import com.hegel.core.wrappers.Wrapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;

import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
/**
 * @param <U>
 */
public class MappedMatcher<T, U> extends BaseMatcher<T> {

    private final Matcher<T> matcher;
    private final Function<U, T> transformer;

    @SuppressWarnings("unchecked")
    private MappedMatcher(Matcher<T> matcher) {
        this(matcher, u -> (T) u);
    }

//    @SuppressWarnings("unchecked")
//    MappedMatcher<U, T> preMap(Function<U, T> transformer) {
//        return new MappedMatcher(this, transformer);
//    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object item) {
        return matcher.matches(transformer.apply((U) item));
    }

    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }

    static <T, U> MappedMatcher<T, U> is(T value) {
        return new MappedMatcher<>(Is.is(value));
    }

    public static <T> Matcher<Wrapper<T>> nullable() {
        return null;//new MappedMatcher(Is.is(null));
    }

    // TODO: 4/18/2016 add other matcher static methods
}
