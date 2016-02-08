package com.hegel.reflect.methods;

public interface ObjectMethod<R, C> extends Method<C> {

    static <R, C> ObjectMethod<R, C> wrap(java.lang.reflect.Method method) {
        method.setAccessible(true);
        return () -> method;
    }
}
