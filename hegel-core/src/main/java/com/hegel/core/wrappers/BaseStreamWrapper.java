package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

@FunctionalInterface
public interface BaseStreamWrapper<T, S extends BaseStream<T, S>> extends BaseStream<T, S>, Wrapper<S> {

    @Override
    default Iterator<T> iterator() {
        return toSrc().iterator();
    }

    @Override
    default Spliterator<T> spliterator() {
        return toSrc().spliterator();
    }

    @Override
    default boolean isParallel() {
        return toSrc().isParallel();
    }

    @Override
    default S sequential() {
        return toSrc().sequential();
    }

    @Override
    default S parallel() {
        return toSrc().parallel();
    }

    @Override
    default S unordered() {
        return toSrc().unordered();
    }

    @Override
    default S onClose(Runnable closeHandler) {
        return toSrc().onClose(closeHandler);
    }

    @Override
    default void close() {
        toSrc().close();
    }
}
