package com.hegel.reflect.methods;

import com.hegel.reflect.Class;

@FunctionalInterface
public interface Method<C> {

    java.lang.reflect.Method toSrc();

    @SuppressWarnings("unchecked")
    static <C, M extends Method<C>> M wrap(java.lang.reflect.Method method) {
        java.lang.Class<?> type = method.getReturnType();
        return (M) (type == int.class || type == short.class || type == char.class || type == byte.class ? IntMethod.wrap(method) :
                type == long.class ? LongMethod.wrap(method) :
                        type == double.class || type == float.class ? DoubleMethod.wrap(method) :
                                ObjectMethod.wrap(method));
    }

    @SuppressWarnings("unchecked")
    default <T extends C> Class<T> getType() {
        return Class.wrap((T) toSrc().getDeclaringClass());
    }
}
