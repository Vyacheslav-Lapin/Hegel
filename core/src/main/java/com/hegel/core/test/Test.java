package com.hegel.core.test;

import com.hegel.core.wrappers.Wrapper;
import org.hamcrest.Matcher;

import java.util.Optional;

public interface Test { // TODO: 3/27/2016 Replace with https://github.com/npathai/hamcrest-optional or something like that

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional) {
        return get(tOptional, "");
    }

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional,
                     String message) {
        return tOptional.orElseThrow(() -> new AssertionError(message));
    }

    static <T> Matcher<Wrapper<T>> isWrapperOf(T wrappedObject) {
        return MappedMatcher.is(wrappedObject);
    }

    static <T> Matcher<Wrapper<T>> containsAt(Optional<T> optional) {
        return optional.isPresent() ? MappedMatcher.is(optional.get()): MappedMatcher.nullable();
    }
}
