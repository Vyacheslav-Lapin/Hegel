package com.hegel.core.functions;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Throwable> extends Consumer<T> {

    static <T, E extends Throwable> Consumer<T> toUncheckedConsumer(ExceptionalConsumer<T, E> exceptionalConsumer) {
        return exceptionalConsumer;
    }

    @SuppressWarnings("unused")
    static <T, E extends Throwable> void put(ExceptionalConsumer<T, E> exceptionalConsumer, T param) {
        put(exceptionalConsumer, param, RuntimeException::new);
    }

    static <T, E extends Throwable, E1 extends Throwable> void put(ExceptionalConsumer<T, E> exceptionalConsumer,
                                                                   T param,
                                                                   Function<E, E1> exceptionTransformer) throws E1 {
        try {
            exceptionalConsumer.put(param);
        } catch (Throwable e) {
            //noinspection unchecked
            throw exceptionTransformer.apply((E) e);
        }
    }

    static <T, E extends Throwable> ExceptionalRunnable<E> supply(ExceptionalConsumer<T, E> exceptionalConsumer,
                                                                  T param) {
        return () -> exceptionalConsumer.accept(param);
    }

    static <T, E extends Throwable> Runnable supplyUnchacked(ExceptionalConsumer<T, E> exceptionalConsumer,
                                                             T param) {
        return supply(exceptionalConsumer, param);
    }

    void put(T t) throws E;

    @Override
    default void accept(T t) {
        try {
            put(t);
        } catch (Throwable e) {
            //noinspection unchecked
            ifThrowable((E) e);
        }
    }

    default void ifThrowable(E e) {
        throw new RuntimeException(e);
    }
}
