package com.hegel.core;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface Lambderator<E> {
    boolean tryAdvance(Consumer<? super E> action);

    default Spliterator toSpliterator() {
        Predicate<Consumer<? super E>> tryAdvance = this::tryAdvance;
        return new Spliterator() {
            @Override
            public boolean tryAdvance(Consumer action) {
                return tryAdvance.test(action);
            }

            @Override
            public Spliterator trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
    }
}
