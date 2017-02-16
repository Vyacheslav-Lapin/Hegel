package com.hegel.core.wrappers;

import java.util.Iterator;
import java.util.function.Consumer;

public interface IteratorWrapper<E> extends Iterator<E>, Wrapper<Iterator<E>> {

    static <E> IteratorWrapper<E> wrap(Iterator<E> iterator) {
        return () -> iterator;
    }

    @Override
    default boolean hasNext() {
        return toSrc().hasNext();
    }

    @Override
    default E next() {
        return toSrc().next();
    }

    @Override
    default void remove() {
        toSrc().remove();
    }

    @Override
    default void forEachRemaining(Consumer<? super E> action) {
        toSrc().forEachRemaining(action);
    }
}
