package com.hegel.core.functions;

import com.hegel.core.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionalTest {

    Object o = new Object();
    String exceptionMessage = "Exception message";

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
        assertThrows(SQLException.class, () ->
                Exceptional.throwAsUnchecked(new SQLException()));
    }

    @Test
    @DisplayName("MapValue method works correctly")
    void mapValue() {
        assertEquals(
                Exceptional.withValue(o)
                        .mapValue(Object::toString).getOrThrowUnchecked(),
                o.toString());
    }

    @Test
    @DisplayName("MapException method works correctly")
    void mapException() {
        assertThrows(IOException.class, () ->
                Exceptional.withException(new SQLException())
                        .mapException(e -> new IOException())
                        .getOrThrowUnchecked()
        );
    }

    @Test
    @DisplayName("Map method works correctly")
    void map() {
        assertEquals(
                Exceptional.withValue(o)
                        .map(
                                Object::toString,
                                e -> new IOException(e.getMessage()))
                        .getOrThrowUnchecked(),
                o.toString());

        assertThat(
                assertThrows(IOException.class, () ->
                        Exceptional.withException(new SQLException(exceptionMessage))
                                .map(
                                        Object::toString,
                                        e -> new IOException(e.getMessage()))
                                .getOrThrowUnchecked())
                        .getMessage(),
                is(exceptionMessage));
    }

    @Test
    @DisplayName("GetOrThrow method works correctly")
    void getOrThrow() throws Exception {
        assertThrows(SQLException.class, () -> {
            Exceptional.withException(new SQLException())
                    .getOrThrow();
            fail("SQLException did not thrown!");
        });
        assertEquals(Exceptional.withValue(o).getOrThrow(), o);
    }

    @Test
    @DisplayName("GetOrThrow(mapper) method works correctly")
    void getOrThrowWithMapper() {
        assertThat(
                assertThrows(Exception.class,() ->
                        Exceptional.withException(
                                new SQLException(exceptionMessage))
                                .getOrThrow(e -> new IOException(e.getMessage())))
                        .getMessage(),
                is(exceptionMessage));
    }

    @Test
    @DisplayName("toOptional method works correctly")
    void toOptional() {
        //noinspection ConstantConditions
        assertThat(Exceptional.withValue(o).toOptional().get(), is(o));

        //noinspection Convert2MethodRef,ConstantConditions,ResultOfMethodCallIgnored
        assertThrows(NoSuchElementException.class, () ->
                Exceptional.withException(new Exception()).toOptional().get());
    }
}