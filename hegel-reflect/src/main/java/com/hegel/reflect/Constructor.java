package com.hegel.reflect;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface Constructor<C> extends Executable<C, C, java.lang.reflect.Constructor<C>> {

    @SuppressWarnings("unchecked")
    static <C> Constructor<C> wrap(java.lang.reflect.Constructor<?> constructor) {
        return () -> (java.lang.reflect.Constructor<C>) constructor;
    }

    @Override
    default C execute(C thisObject, Object... params) throws InvocationTargetException, IllegalAccessException {
        return execute(params);
    }

    @Override
    @SneakyThrows
    default C execute(Object... params) {
        assert paramTypesCheck(params);
        return get().newInstance(params);
    }

    @Override
    default Class<C> getType() {
        return getOwnerClass();
    }
}
