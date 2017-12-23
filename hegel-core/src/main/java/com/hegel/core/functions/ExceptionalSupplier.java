package com.hegel.core.functions;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Exception> extends Supplier<Exceptional<T, E>>, Callable<T> {

    static <T, E extends Exception> Exceptional<T, E> call(ExceptionalSupplier<T, E> exceptionalSupplier) {
        return exceptionalSupplier.get();
    }

    static <T, E extends Exception> T getOrThrowUnchecked(ExceptionalSupplier<T, E> exceptionalSupplier) {
        return call(exceptionalSupplier).getOrThrowUnchecked();
    }

    static <T> T avoid(Object t) {
        return null;
    }

    static <T, E extends Exception> Supplier<T> toUncheckedSupplier(
            ExceptionalSupplier<T, E> exceptionalSupplier) {
        return exceptionalSupplier::getOrThrowUnchecked;
    }

    T call() throws E;

    @Override
    @SuppressWarnings("unchecked")
    default Exceptional<T, E> get() {
        try {
            return Exceptional.withValue(call());
        } catch (Exception e) {
            return Exceptional.withException((E) e);
        }
    }

    default T getOrThrowUnchecked() {
        return get().getOrThrowUnchecked();
    }

    default T executeOrThrowUnchecked() {
        return get().getOrThrowUnchecked();
    }

    default Optional<T> getAsOptional() {
        return get().toOptional();
    }
}
