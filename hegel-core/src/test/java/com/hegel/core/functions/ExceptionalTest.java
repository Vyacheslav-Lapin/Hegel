package com.hegel.core.functions;

import com.hegel.core.Either;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionalTest {

    Object o = new Object();

    @Test
    @DisplayName("wrap method works correctly")
    void wrap() {
        assertThat(
                Exceptional.wrap(
                        Either.left(o)).getOrThrowUnchecked(),
                is(o));
    }

    @Test
    @DisplayName("getOrThrowUnchecked method works correctly")
    void getOrThrowUnchecked() {
        assertThrows(SQLException.class, () ->
                Exceptional.withException(new SQLException())
                        .getOrThrowUnchecked());
    }

    @Test
    @DisplayName("withValue method works correctly")
    void withValue() {
        assertEquals(Exceptional.withValue(o).getOrThrowUnchecked(), o);
    }

    @Test
    @DisplayName("withException method works correctly")
    void withException() {
        assertThrows(SQLException.class, () ->
                Exceptional.withException(new SQLException())
                        .getOrThrowUnchecked());
    }

    @Test
    @DisplayName("ThrowAsUnchecked method works correctly")
    void throwAsUnchecked() {

    }

    @Test
    @DisplayName("MapValue method works correctly")
    void mapValue() {
    }

    @Test
    @DisplayName("MapException method works correctly")
    void mapException() {
    }

    @Test
    @DisplayName("Map method works correctly")
    void map() {
    }

    @Test
    @DisplayName("GetOrThrow method works correctly")
    void getOrThrow() {
    }

    @Test
    @DisplayName("GetOrThrow1 method works correctly")
    void getOrThrow1() {
    }

    @Test
    @DisplayName("ToOptional method works correctly")
    void ToOptional() {
    }
}