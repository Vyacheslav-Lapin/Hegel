package com.hegel.reflect.methods;

public interface IntMethod<C> extends Method<C> {

    static <C> IntMethod<C> wrap(java.lang.reflect.Method method) {
        Class<?> type = method.getReturnType();
        assert type == int.class || type == short.class || type == char.class || type == byte.class;

        return () -> method;
    }
}
