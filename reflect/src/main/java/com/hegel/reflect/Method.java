package com.hegel.reflect;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface Method<T, C> extends Executable<T, C, java.lang.reflect.Method> {

    @SuppressWarnings("unchecked")
    @Override
    default Class<T> getType() {
        return Class.wrap((java.lang.Class<T>) toSrc().getReturnType());
    }

    @SuppressWarnings("unchecked")
    static <T, C> Method<T, C> wrap(java.lang.reflect.Method method) {
        return () -> method;
    }

    @Override
    default boolean isPrimitive() {
        return toSrc().getReturnType().isPrimitive();
    }

    @Override
    @SuppressWarnings("unchecked")
    default T execute(C thisObject, Object... params) throws InvocationTargetException, IllegalAccessException {
        assert paramTypesCheck(params);
        return (T) toSrc().invoke(thisObject, params);
    }
}
