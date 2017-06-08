package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface IterableWrapper<T> extends Iterable<T>, Supplier<Iterable<T>> {

    static <T> IterableWrapper<T> wrap(Iterable<T> iterable) {
        return () -> iterable;
    }

    @Override
    default Iterator<T> iterator() {
        return get().iterator();
    }

    @Override
    default Spliterator<T> spliterator() {
        return get().spliterator();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        get().forEach(action);
    }
}
