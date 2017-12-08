package com.hegel.core.functions;

import java.util.function.Predicate;

@FunctionalInterface
public interface ExceptionalPredicate<T, E extends Exception> extends Predicate<T> {

    static <T, E extends Exception> boolean call(T t, ExceptionalPredicate<T, E> exceptionalPredicate) {
        return exceptionalPredicate.test(t);
    }

    boolean is(T t) throws E;

    @Override
    default boolean test(T t) {
        try {
            return is(t);
        } catch (Exception e) {
            //noinspection unchecked
            return mapException((E) e);
        }
    }

    /**
     * Override this method if you wanna interpret some of exception as a result
     */
    default boolean mapException(E e) {
        Exceptional.throwAsUnchecked(e);
        return false; // never used! Just for pass the compiler check...
    }
}
