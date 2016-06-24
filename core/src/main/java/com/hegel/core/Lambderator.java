package com.hegel.core;

import java.util.function.Consumer;

@FunctionalInterface
public interface Lambderator<E> {
    boolean tryAdvance(Consumer<? super E> action);
}
