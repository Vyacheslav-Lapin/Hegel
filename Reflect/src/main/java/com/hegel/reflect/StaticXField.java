package com.hegel.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StaticXField<F, C> extends XField<F, C> {

    public StaticXField(Field field) {
        super(field);
    }

    public StaticXField(Field field, Class<F> type) {
        super(field, type);
    }

    public StaticXField(Field field, Class<F> type, Class<C> declaringClass) {
        super(field, type, declaringClass);
    }

    public static <F, C> StaticXField<F, C> from(Field field, Class<F> type, Class<C> declaringClass) {
        assert Modifier.isStatic(field.getModifiers());
        assert field.getType().equals(type);
        assert field.getDeclaringClass().equals(declaringClass);

        return new StaticXField<>(field, type, declaringClass);
    }

    @SuppressWarnings("unchecked")
    public static <F, C> StaticXField<F, C> from(Field field) {
        assert Modifier.isStatic(field.getModifiers());
        return from(field, (Class<F>) field.getType(), (Class<C>) field.getDeclaringClass());
    }

    public F getValue() {
        return getValue(null);
    }
}
