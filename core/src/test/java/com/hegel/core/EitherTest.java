package com.hegel.core;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EitherTest {

    private Object object = new Object();
    private Either<?,?> eitherLeft = Either.left(object);
    private Either<?,?> eitherRight = Either.right(object);

    @Test
    public void leftWorksCorrectly() throws Exception {
        assertThat(eitherLeft.left(), is(object));
        assertThat(eitherRight.left(), nullValue());
    }

    @Test
    public void rightWorksCorrectly() throws Exception {
        assertThat(eitherRight.right(), is(object));
        assertThat(eitherLeft.right(), nullValue());
    }

    @Test
    public void isLeftWorksCorrectly() throws Exception {
        assertThat(eitherLeft.isLeft(), is(true));
        assertThat(eitherRight.isLeft(), is(false));
    }

    @Test
    public void isRightWorksCorrectly() throws Exception {
        assertThat(eitherRight.isRight(), is(true));
        assertThat(eitherLeft.isRight(), is(false));
    }

    @Test
    public void peekLeftWorksCorrectly() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();

        eitherRight.peekLeft(o ->  byteArrayOutputStream.write(2));
        assertThat(byteArrayOutputStream.toByteArray().length, is(0));

        eitherLeft.peekLeft(o ->  byteArrayOutputStream.write(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte)1));
    }

    @Test
    public void peekRightWorksCorrectly() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();

        eitherLeft.peekRight(o ->  byteArrayOutputStream.write(2));
        assertThat(byteArrayOutputStream.toByteArray().length, is(0));

        eitherRight.peekRight(o ->  byteArrayOutputStream.write(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte)1));
    }

    @Test
    public void peekWorksCorrectly() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();

        eitherRight.peek(
                o ->  byteArrayOutputStream.write(2),
                o ->  byteArrayOutputStream.write(1));

        assertThat(byteArrayOutputStream.toByteArray().length, is(1));
        assertThat(byteArrayOutputStream.toByteArray()[0], is((byte)1));
    }

    @Test
    public void mapLeftWorksCorrectly() throws Exception {
        Optional<String> optionalLeft = eitherLeft.mapLeft(Object::toString).optionalLeft();
        if (optionalLeft.isPresent())
            assertTrue(optionalLeft.get().equals(object.toString()));
        else
            Assert.fail();
    }

    @Test
    public void mapRightWorksCorrectly() throws Exception {
        Optional<String> optionalRight = eitherRight.mapRight(Object::toString).optionalRight();
        if (optionalRight.isPresent())
            assertTrue(optionalRight.get().equals(object.toString()));
        else
            Assert.fail();
    }

    @Test
    public void mapWorksCorrectly() throws Exception {
        Optional<String> optionalRight = eitherRight.map(Object::toString, Object::toString).optionalRight();
        if (optionalRight.isPresent())
            assertTrue(optionalRight.get().equals(object.toString()));
        else
            Assert.fail();
    }

    @Test
    @Ignore
    public void swapWorksCorrectly() throws Exception {

    }

    @Test
    @Ignore
    public void optionalLeftWorksCorrectly() throws Exception {

    }

    @Test
    @Ignore
    public void optionalRightWorksCorrectly() throws Exception {

    }

    @Test
    @Ignore
    public void foldWorksCorrectly() throws Exception {

    }
}