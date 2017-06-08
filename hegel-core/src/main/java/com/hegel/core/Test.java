package com.hegel.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;
import java.util.function.Supplier;

public interface Test { // TODO: 3/27/2016 Replace with https://github.com/npathai/hamcrest-optional or something like that

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional) {
        return get(tOptional, "");
    }

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional,
                     String message) {
        return tOptional.orElseThrow(() -> new AssertionError(message));
    }

    @SuppressWarnings("unused")
    static <T> Matcher<Supplier<T>> isWrapperOf(T wrappedObject) {
        return new TypeSafeMatcher<Supplier<T>>() {
            @Override
            protected boolean matchesSafely(Supplier<T> item) {
                return item.get().equals(wrappedObject);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("wrapped ").appendValue(wrappedObject);
            }
        };
    }

    @SuppressWarnings("unused")
    static <T> Matcher<Optional<T>> contains(T expected) {
        return new TypeSafeMatcher<Optional<T>>() {

            @Override
            protected boolean matchesSafely(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> item) {
                return item.isPresent() && expected.equals(item.get());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains ").appendValue(expected);
            }
        };
    }
}
