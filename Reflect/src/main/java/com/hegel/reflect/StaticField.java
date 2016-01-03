package com.hegel.reflect;

import java.lang.reflect.Modifier;

public interface StaticField<F, C> extends Field<F, C> {

    java.lang.reflect.Field toSrc();

    @SuppressWarnings("unchecked")
    static <F, C> StaticField<F, C> wrap(java.lang.reflect.Field field) {
        assert Modifier.isStatic(field.getModifiers());
        return () -> field;
    }

    default F getValue() {
        return getValue(null);
    }
}
