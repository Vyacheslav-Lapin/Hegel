package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IteratorWrapper<E> extends Iterator<E>, Supplier<Iterator<E>> {

    static <E> IteratorWrapper<E> wrap(Iterator<E> iterator) {
        return () -> iterator;
    }

    @Override
    default boolean hasNext() {
        return get().hasNext();
    }

    @Override
    default E next() {
        return get().next();
    }

    @Override
    default void remove() {
        get().remove();
    }

    @Override
    default void forEachRemaining(Consumer<? super E> action) {
        get().forEachRemaining(action);
    }
}
