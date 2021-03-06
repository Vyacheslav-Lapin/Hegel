package com.hegel.core.functions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface ExceptionalVarFunction<T, R, E extends Exception> extends VarFunction<T, Exceptional<R, E>>, ExceptionalFunction<T[], R, E> {

    @NotNull
    @SafeVarargs
    @Contract(pure = true)
    static <T, R, E extends Exception> ExceptionalSupplier<R, E> supply(ExceptionalVarFunction<T, R, E> exceptionalVarFunction, T... params) {
        return () -> exceptionalVarFunction.map(params);
    }

    @NotNull
    @Contract(pure = true)
    static <T, R, E extends Exception> Supplier<R> supplyUnchecked(ExceptionalVarFunction<T, R, E> exceptionalVarFunction, T... params) {
        return supply(exceptionalVarFunction, params)::getOrThrowUnchecked;
    }

    @SuppressWarnings("unchecked")
    R map(T... t) throws E;

    @Override
    @SuppressWarnings("unchecked")
    default Exceptional<R, E> apply(T... params) {
        try {
            return Exceptional.withValue(map(params));
        } catch (Exception e) {
            return Exceptional.withException((E) e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    default ExceptionalSupplier<R, E> supply(T... params) {
        return ExceptionalFunction.super.supply(params);
    }

    default R getOrThrowUnchecked(T... params) {
        return apply(params).getOrThrowUnchecked();
    }

    @SuppressWarnings("unchecked")
    default void executeOrThrowUnchecked(T... params) {
        getOrThrowUnchecked(params);
    }
}
