package com.hegel.core.functions;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionalVarConsumer<T, E extends Exception> extends VarConsumer<T>, ExceptionalConsumer<T[], E> {

    static <T, E extends Exception> VarConsumer<T> toUnchecked(ExceptionalVarConsumer<T, E> exceptionalConsumer) {
        return exceptionalConsumer;
    }

    @SafeVarargs
    @SuppressWarnings("unused")
    static <T, E extends Exception> void put(ExceptionalVarConsumer<T, E> exceptionalConsumer, T... params) {
        put(exceptionalConsumer, RuntimeException::new, params);
    }

    @SafeVarargs
    static <T, E extends Exception, E1 extends Exception> void put(ExceptionalVarConsumer<T, E> teExceptionalVarConsumer,
                                                                   Function<E, E1> exceptionTransformer,
                                                                   T... params) throws E1 {
        try {
            teExceptionalVarConsumer.put(params);
        } catch (Exception e) {
            //noinspection unchecked
            throw exceptionTransformer.apply((E) e);
        }
    }

    @SafeVarargs
    static <T, E extends Exception> ExceptionalRunnable<E> supply(ExceptionalVarConsumer<T, E> exceptionalVarConsumer,
                                                                  T... params) {
        return () -> exceptionalVarConsumer.accept(params);
    }

    @SafeVarargs
    static <T, E extends Exception> Runnable supplyUnchacked(ExceptionalVarConsumer<T, E> exceptionalVarConsumer,
                                                             T... params) {
        return supply(exceptionalVarConsumer, params);
    }

    @SuppressWarnings("unchecked")
    void put(T... params) throws E;

    @Override
    @SuppressWarnings("unchecked")
    default void accept(T... params) {
        try {
            put(params);
        } catch (Exception e) {
            //noinspection unchecked
            ifException((E) e);
        }
    }
}
