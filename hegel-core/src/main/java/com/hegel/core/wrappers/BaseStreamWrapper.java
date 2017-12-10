package com.hegel.core.wrappers;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.BaseStream;

@FunctionalInterface
public interface BaseStreamWrapper<T, S extends BaseStream<T, S>> extends BaseStream<T, S>, Supplier<S> {

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return get().iterator();
    }

    @NotNull
    @Override
    default Spliterator<T> spliterator() {
        return get().spliterator();
    }

    @Override
    default boolean isParallel() {
        return get().isParallel();
    }

    @NotNull
    @Override
    default S sequential() {
        return get().sequential();
    }

    @NotNull
    @Override
    default S parallel() {
        return get().parallel();
    }

    @NotNull
    @Override
    default S unordered() {
        return get().unordered();
    }

    @NotNull
    @Override
    default S onClose(Runnable closeHandler) {
        return get().onClose(closeHandler);
    }

    @Override
    default void close() {
        get().close();
    }
}
