package com.hegel.reflect;

import java.util.function.Supplier;

/**
 * @param <T> type of parameter
 * @param <E> method or constructor
 */
@FunctionalInterface
public interface Parameter<T, E extends Executable<?, ?, ?>> extends Supplier<java.lang.reflect.Parameter> {

    static Parameter wrap(java.lang.reflect.Parameter parameter) {
        return () -> parameter;
    }

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return Class.wrap((java.lang.Class<T>) get().getType());
    }

    default String getName() {
        return get().getName();
    }

    @SuppressWarnings("unchecked")
    default E getExecutable() {
        return (E) get().getDeclaringExecutable();
    }
}
