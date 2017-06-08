package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.BaseStream;

@FunctionalInterface
public interface BaseStreamWrapper<T, S extends BaseStream<T, S>> extends BaseStream<T, S>, Supplier<S> {

    @Override
    default Iterator<T> iterator() {
        return get().iterator();
    }

    @Override
    default Spliterator<T> spliterator() {
        return get().spliterator();
    }

    @Override
    default boolean isParallel() {
        return get().isParallel();
    }

    @Override
    default S sequential() {
        return get().sequential();
    }

    @Override
    default S parallel() {
        return get().parallel();
    }

    @Override
    default S unordered() {
        return get().unordered();
    }

    @Override
    default S onClose(Runnable closeHandler) {
        return get().onClose(closeHandler);
    }

    @Override
    default void close() {
        get().close();
    }
}
