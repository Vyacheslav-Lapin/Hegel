package com.hegel.core.functions;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@FunctionalInterface
@SuppressWarnings({"WeakerAccess", "unused"})
public interface ExceptionalBooleanSupplier<E extends Exception> extends BooleanSupplier {

    static <E extends Exception> boolean call(ExceptionalBooleanSupplier<E> exceptionalBooleanSupplier) {
        return exceptionalBooleanSupplier.getAsBoolean();
    }

    boolean is() throws E;

    @Override
    default boolean getAsBoolean() {
        return getAsBoolean(this::mapException);
    }

    default boolean getAsBoolean(Predicate<E> exceptionMapper) {
        try {
            return is();
        } catch (Exception e) {
            //noinspection unchecked
            return exceptionMapper.test((E) e);
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
