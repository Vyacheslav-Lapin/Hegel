package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

@FunctionalInterface
public interface IterableWrapper<T> extends Iterable<T>, Wrapper<Iterable<T>> {

    static <T> IterableWrapper<T> wrap(Iterable<T> iterable) {
        return () -> iterable;
    }

    @Override
    default Iterator<T> iterator() {
        return toSrc().iterator();
    }

    @Override
    default Spliterator<T> spliterator() {
        return toSrc().spliterator();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        toSrc().forEach(action);
    }
}
