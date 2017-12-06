package com.hegel.core.functions;

import com.hegel.core.Either;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionalTest {

    @Test
    void wrap() {
        Object o = new Object();
        assertThat(
                Exceptional.wrap(
                        Either.left(o)).getOrThrowUnchecked(),
                is(o));
    }

    @Test
    void getOrThrowUnchecked() {
        assertThrows(SQLException.class, () ->
                Exceptional.withException(new SQLException())
                        .getOrThrowUnchecked());
    }
}