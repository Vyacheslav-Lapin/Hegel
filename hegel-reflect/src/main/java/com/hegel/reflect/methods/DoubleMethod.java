package com.hegel.reflect.methods;

public interface DoubleMethod<C> extends Method<C> {

    static <C> DoubleMethod<C> wrap(java.lang.reflect.Method method) {
        Class<?> type = method.getReturnType();
        assert type == double.class || type == float.class;

        method.setAccessible(true);

        return () -> method;
    }
}
