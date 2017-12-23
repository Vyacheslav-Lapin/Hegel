package com.hegel.core.functions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ExceptionalBiFunction<T, U, R, E extends Exception> extends BiFunction<T, U, Exceptional<R, E>> {

    static <T, U, R, E extends Exception> R getOrThrowUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            T param1, U param2) {
        return exceptionalBiFunction.getOrThrowUnchecked(param1, param2);
    }

    static <T, U, R, E extends Exception> BiFunction<T, U, R> toUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction::getOrThrowUnchecked;
    }

    static <T, U, R, E extends Exception> Function<T, Function<U, ExceptionalSupplier<R, E>>> curry(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction.curry();
    }

    static <T, U, R, E extends Exception> Function<T, Function<U, Supplier<R>>> curryUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction.curryUnchecked();
    }

    static <T, U, R, E extends Exception> ExceptionalSupplier<R, E> supply(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction, T param1, U param2) {
        return exceptionalBiFunction.supply(param1, param2);
    }

    static <T, U, R, E extends Exception> Supplier<R> supplyUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            T param1, U param2) {
        return exceptionalBiFunction.supplyUnchecked(param1, param2);
    }

    static <T, U, R, E extends Exception> Function<U, ExceptionalSupplier<R, E>> partialFirst(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            T param1) {
        return exceptionalBiFunction.partialFirst(param1);
    }

    static <T, U, R, E extends Exception> Function<U, Supplier<R>> partialFirstUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            T param1) {
        return exceptionalBiFunction.partialFirstUnchecked(param1);
    }

    static <T, U, R, E extends Exception> Function<T, ExceptionalSupplier<R, E>> partialSecond(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            U param2) {
        return exceptionalBiFunction.partialSecond(param2);
    }

    static <T, U, R, E extends Exception> Function<T, Supplier<R>> partialSecondUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
            U param2) {
        return exceptionalBiFunction.partialSecondUnchecked(param2);
    }

    R reduce(T t, U u) throws E;

    @Override
    default Exceptional<R, E> apply(T t, U u) {
        return apply(t, u, this::mapException);
    }

    default Exceptional<R, E> apply(T t, U u, Function<E, Exceptional<R, E>> exceptionMapper) {
        try {
            return Exceptional.withValue(reduce(t, u));
        } catch (Exception e) {
            //noinspection unchecked
            return exceptionMapper.apply((E) e);
        }
    }

    /**
     * Override this method if you wanna interpret some of exception as a result
     */
    default Exceptional<R, E> mapException(E e) {
        return Exceptional.withException(e);
    }

    default R getOrThrowUnchecked(T param1, U param2) {
        return apply(param1, param2).getOrThrowUnchecked();
    }

    default BiFunction<T, U, R> toUnchecked() {
        return this::getOrThrowUnchecked;
    }

    default Function<T, Function<U, ExceptionalSupplier<R, E>>> curry() {
        return param1 -> param2 -> () -> this.reduce(param1, param2);
    }

    default Function<T, Function<U, Supplier<R>>> curryUnchecked() {
        return param1 -> param2 -> () -> this.apply(param1, param2).getOrThrowUnchecked();
    }

    default ExceptionalSupplier<R, E> supply(T param1, U param2) {
        return () -> reduce(param1, param2);
    }

    default Supplier<R> supplyUnchecked(T param1, U param2) {
        return supply(param1, param2)::getOrThrowUnchecked;
    }

    default Function<U, ExceptionalSupplier<R, E>> partialFirst(T param1) {
        return param2 -> () -> reduce(param1, param2);
    }

    default Function<U, Supplier<R>> partialFirstUnchecked(T param1) {
        return param2 -> () -> getOrThrowUnchecked(param1, param2);
    }

    default Function<T, ExceptionalSupplier<R, E>> partialSecond(U param2) {
        return param1 -> () -> reduce(param1, param2);
    }

    default Function<T, Supplier<R>> partialSecondUnchecked(U param2) {
        return param1 -> () -> getOrThrowUnchecked(param1, param2);
    }
}
