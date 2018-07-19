package com.hegel.core;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;

public interface TestUtils {

    String TEST_RESOURCES_PATH = "./src/test/resources/";
    String LINE_SEPARATOR = System.getProperty("line.separator");

    @NotNull
    @Contract(pure = true)
    static String toTestResourceFilePath(String fileName) {
        return TEST_RESOURCES_PATH + fileName;
    }

    @NotNull
    @SneakyThrows
    static String fromSystemOut(@NotNull Runnable task) {
        PrintStream realOut = System.out;
        val out = new ByteArrayOutputStream();
        @Cleanup val printStream = new PrintStream(out);
        System.setOut(printStream);
        task.run();
        System.setOut(realOut);
        return new String(out.toByteArray()).intern();
    }

    static String[] fromSystemOutPrintln (Runnable task) {
        return fromSystemOut(task).split(LINE_SEPARATOR);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static <T> Matcher<Supplier<T>> isWrapperOf(T wrappedObject) {
        return new TypeSafeMatcher<>() {
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

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static <T> Matcher<Optional<T>> contains(T expected) {
        return new TypeSafeMatcher<>() {

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
