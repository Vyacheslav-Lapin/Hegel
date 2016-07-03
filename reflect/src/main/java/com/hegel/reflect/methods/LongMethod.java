package com.hegel.reflect.methods;

public interface LongMethod<C> extends Method<C> {

    static <C> LongMethod<C> wrap(java.lang.reflect.Method method) {
        assert method.getReturnType() == long.class;
        return () -> method;
    }
}
