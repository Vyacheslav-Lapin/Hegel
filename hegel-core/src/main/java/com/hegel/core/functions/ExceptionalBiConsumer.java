package com.hegel.core.functions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface ExceptionalBiConsumer<T, U, E extends Exception> extends BiConsumer<T, U> {

    static <T, U, E extends Exception> void executeOrThrowUnchecked(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer,
            T param1, U param2) {
        exceptionalBiConsumer.accept(param1, param2);
    }

    static <T, U, E extends Exception> Function<T, Function<U, ExceptionalRunnable<E>>> curry(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer) {
        return exceptionalBiConsumer.curry();
    }

    static <T, U, E extends Exception> Function<T, Function<U, Runnable>> curryUnchecked(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer) {
        return exceptionalBiConsumer.curryUnchecked();
    }

    static <T, U, E extends Exception> ExceptionalRunnable<E> toRunnable(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer, T param1, U param2) {
        return exceptionalBiConsumer.toRunnable(param1, param2);
    }

    static <T, U, E extends Exception> Runnable toRunnableUnchecked(
            ExceptionalBiConsumer<T, U, E> exceptionalBiFunction,
            T param1, U param2) {
        return exceptionalBiFunction.toRunnableUnchecked(param1, param2);
    }

    static <T, U, E extends Exception> ExceptionalConsumer<U, E> partialFirst(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer,
            T firstParam) {
        return exceptionalBiConsumer.partialFirst(firstParam);
    }

    static <T, U, E extends Exception> Consumer<U> partialFirstUnchecked(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer,
            T firstParam) {
        return exceptionalBiConsumer.partialFirstUnchecked(firstParam);
    }

    static <T, U, E extends Exception> ExceptionalConsumer<T, E> partialSecond(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer,
            U secondParam) {
        return exceptionalBiConsumer.partialSecond(secondParam);
    }

    static <T, U, E extends Exception> Consumer<T> partialSecondUnchecked(
            ExceptionalBiConsumer<T, U, E> exceptionalBiConsumer,
            U secondParam) {
        return exceptionalBiConsumer.partialSecondUnchecked (secondParam);
    }

    void put(T t, U u) throws E;

    @Override
    default void accept(T t, U u) {
        try {
            put(t, u);
        } catch (Exception e) {
            //noinspection unchecked
            ifException((E) e);
        }
    }

    /**
     * Override this method if you wanna catch some of exception
     */
    default void ifException(E e) {
        Exceptional.throwAsUnchecked(e);
    }

    default Function<T, Function<U, ExceptionalRunnable<E>>> curry() {
        return param1 -> param2 -> () -> put(param1, param2);
    }

    default Function<T, Function<U, Runnable>> curryUnchecked() {
        return param1 -> param2 -> () -> accept(param1, param2);
    }

    default ExceptionalConsumer<U, E> partialFirst(T firstParam) {
        return u -> put(firstParam, u);
    }

    default Consumer<U> partialFirstUnchecked(T firstParam) {
        return u -> accept(firstParam, u);
    }

    default ExceptionalConsumer<T, E> partialSecond(U secondParam) {
        return t -> put(t, secondParam);
    }

    default Consumer<T> partialSecondUnchecked(U secondParam) {
        return t -> accept(t, secondParam);
    }

    default ExceptionalRunnable<E> toRunnable(T firstParam, U secondParam) {
        return () -> put(firstParam, secondParam);
    }

    default Runnable toRunnableUnchecked(T firstParam, U secondParam) {
        return () -> accept(firstParam, secondParam);
    }
}
