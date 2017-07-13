package com.hegel.core;

import lombok.SneakyThrows;
import lombok.val;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;

public interface Test {

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional) {
        return get(tOptional, "");
    }

    static <T> T get(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> tOptional,
                     String message) {
        return tOptional.orElseThrow(() -> new AssertionError(message));
    }

    @SneakyThrows
    static String fromSystemOut(Runnable runnable) {

        PrintStream realOut = System.out;

        try (val out = new ByteArrayOutputStream();
             val printStream = new PrintStream(out)) {

            System.setOut(printStream);
            runnable.run();

            return new String(out.toByteArray()).intern();

        } finally {
            System.setOut(realOut);
        }
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
