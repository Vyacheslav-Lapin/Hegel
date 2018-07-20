package com.hegel.core;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

import static com.hegel.core.TestUtils.*;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class TestUtilsTest {

    static String s = "Lorem ipsum dolor sit amet.";

    @Test
    @SneakyThrows
    @DisplayName("\"ToTestResourceFilePath\" method works correctly")
    void testToTestResourceFilePath() {
        assertTrue(new File(toTestResourceFilePath("log4j2.xml")).exists());
    }

    @Test
    @SneakyThrows
    @DisplayName("\"FromSystemOut\" method works correctly")
    void testFromSystemOut() {
        // given
        Runnable task = () -> System.out.print(s);

        // when // then
        assertThat(fromSystemOut(task)).matches(s);
    }

    @Test
    @SneakyThrows
    @DisplayName("\"FromSystemOutPrintln\" method works correctly")
    void testFromSystemOutPrintln() {
        // given
        Runnable task = () -> {
            System.out.println(s);
            System.out.println("123");
            System.out.println("456");
        };

        // when // then
        assertThat(fromSystemOutPrintln(task)).contains(s, "123", "456");
    }

    @Test
    @SneakyThrows
    @DisplayName("\"IsWrapperOf\" method works correctly")
    void testIsWrapperOf() {
        // given
        Supplier<Integer> integerSupplier = () -> 1;

        // when // then
        MatcherAssert.assertThat(integerSupplier, isWrapperOf(1));
    }

    @Test
    @SneakyThrows
    @DisplayName("\"Contains\" method works correctly")
    void testContains() {
        // given
        val optionalInteger = Optional.of(1);

        // when // then
        MatcherAssert.assertThat(optionalInteger, contains(1));
    }
}