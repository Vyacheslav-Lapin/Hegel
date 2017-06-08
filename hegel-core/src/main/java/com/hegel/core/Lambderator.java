package com.hegel.core;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface Lambderator<E> extends Spliterator<E> {

    static <E> Lambderator<E> from(Lambderator<E> lambderator) {
        return lambderator;
    }

    @Override
    default Spliterator<E> trySplit() {
        return null;
    }

    @Override
    default long estimateSize() {
        return 0;
    }

    @Override
    default int characteristics() {
        return 0;
    }

    default Stream<E> stream(boolean parallel) {
        return StreamSupport.stream(this, parallel);
    }

    default Stream<E> stream() {
        return stream(false);
    }

    static <E> Stream<E> stream(Lambderator<E> lambderator, boolean parallel) {
        return lambderator.stream(parallel);
    }

    static <E> Stream<E> stream(Lambderator<E> lambderator) {
        return lambderator.stream();
    }
}
