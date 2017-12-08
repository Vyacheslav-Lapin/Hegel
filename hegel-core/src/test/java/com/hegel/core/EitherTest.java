package com.hegel.core;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EitherTest {

    private Object object = new Object();
    private Either<?, ?> eitherLeft = Either.left(object);
    private Either<?, ?> eitherRight = Either.right(object);

    @SuppressWarnings("unchecked")
    @Test
    void left() {
        new Random().ints(20, 0, 2)
                .mapToObj(i -> i == 0
                        ? Either.<String, Integer>left("left value (String)")
                        : Either.<String, Integer>right(42))
                .forEach(either -> either.apply(
                        left -> System.out.println("received left value: " + left.substring(11)),
                        right -> System.out.println("received right value: 0x" + Integer.toHexString(right))
                ));
    }

    @Test
    void leftWorksCorrectly() {
        assertThat(eitherLeft.left(), is(object));
        assertThat(eitherRight.left(), nullValue());
    }

    @Test
    void rightWorksCorrectly() {
        assertThat(eitherRight.right(), is(object));
        assertThat(eitherLeft.right(), nullValue());
    }

    @Test
    void isLeftWorksCorrectly() {
        assertThat(eitherLeft.isLeft(), is(true));
        assertThat(eitherRight.isLeft(), is(false));
    }

    @Test
    void isRightWorksCorrectly() {
        assertThat(eitherRight.isRight(), is(true));
        assertThat(eitherLeft.isRight(), is(false));
    }

    @Test
    void peekLeftWorksCorrectly() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        eitherRight.peekLeft(o -> byteArrayOutputStream.write(2));
        assertThat(byteArrayOutputStream.toByteArray().length, is(0));

        eitherLeft.peekLeft(o -> byteArrayOutputStream.write(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte) 1));
    }

    @Test
    void peekRightWorksCorrectly() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        eitherLeft.peekRight(o -> byteArrayOutputStream.write(2));
        assertThat(byteArrayOutputStream.toByteArray().length, is(0));

        eitherRight.peekRight(o -> byteArrayOutputStream.write(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte) 1));
    }

    @Test
    void peekWorksCorrectly() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        eitherRight.peek(
                o -> byteArrayOutputStream.write(2),
                o -> byteArrayOutputStream.write(1));

        assertThat(byteArrayOutputStream.toByteArray().length, is(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte) 1));
    }

    @Test
    void mapLeftWorksCorrectly() {
        Optional<String> optionalLeft = eitherLeft.mapLeft(Object::toString).optionalLeft();
        assertTrue(optionalLeft.isPresent() && optionalLeft.get().equals(object.toString()));
    }

    @Test
    void mapRightWorksCorrectly() {
        Optional<String> optionalRight = eitherRight.mapRight(Object::toString).optionalRight();
        assertTrue(optionalRight.isPresent() && optionalRight.get().equals(object.toString()));
    }

    @Test
    void mapWorksCorrectly() {
        Optional<String> optionalRight = eitherRight.map(Object::toString, Object::toString).optionalRight();
        assertTrue(optionalRight.isPresent() && optionalRight.get().equals(object.toString()));
    }

    @Test
    void swapWorksCorrectly() {

    }

    @Test
    void optionalLeftWorksCorrectly() {

    }

    @Test
    void optionalRightWorksCorrectly() {

    }

    @Test
    void foldWorksCorrectly() {

    }
}