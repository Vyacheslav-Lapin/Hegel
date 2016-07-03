package com.hegel.reflect;

import com.hegel.core.wrappers.Wrapper;

@FunctionalInterface
public interface Parameter<T, C, E extends java.lang.reflect.Executable> extends Wrapper<java.lang.reflect.Parameter> {

    static Parameter wrap(java.lang.reflect.Parameter parameter) {
        return () -> parameter;
    }

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return Class.wrap((java.lang.Class<T>) toSrc().getType());
    }

    default String getName() {
        return toSrc().getName();
    }

    @SuppressWarnings("unchecked")
    default E getExecutable() {
        return (E) toSrc().getDeclaringExecutable();
    }

    @SuppressWarnings("unchecked")
    default C getDeclaredClass() {
        return (C) getExecutable().getDeclaringClass();
    }
}
