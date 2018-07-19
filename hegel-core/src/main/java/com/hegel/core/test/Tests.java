package com.hegel.core.test;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public interface Tests {

    String TEST_RESOURCES_PATH = "./src/test/resources/";
    String LINE_SEPARATOR = System.getProperty("line.separator");

    @NotNull
    @SneakyThrows
    @Contract("_ -> new")
    static String fromSystemOutPrint(@NotNull Runnable task) {
        val out = new ByteArrayOutputStream();
        @Cleanup val printStream = new PrintStream(out);
        PrintStream realOut = System.out;
        System.setOut(printStream);
        task.run();
        System.setOut(realOut);

        return new String(out.toByteArray()).intern();
    }

    static String fromSystemOutPrintln(Runnable runnable) {
        String s = fromSystemOutPrint(runnable);
        return s.endsWith(LINE_SEPARATOR) ?
                s.substring(0, s.length() - LINE_SEPARATOR.length()) :
                s;
    }

    @NotNull
    @Contract(pure = true)
    static String toTestResourceFilePath(String fileName) {
        return TEST_RESOURCES_PATH + fileName;
    }

    static TypeSafeMatcher<Number> closeTo(double value) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Number item) {
                return item.doubleValue() == value;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
