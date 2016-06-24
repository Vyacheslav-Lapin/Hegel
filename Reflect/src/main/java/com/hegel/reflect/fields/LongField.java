package com.hegel.reflect.fields;

@FunctionalInterface
public interface LongField<C> extends Field<Long, C> {

    static <C> LongField<C> wrap(java.lang.reflect.Field field) {
        assert field.getType() == long.class;
        field.setAccessible(true);
        return () -> field;
    }

    @Override
    default Long value(C c) {
        return primitiveValue(c);
    }

    default long primitiveValue(C object) {
        try {
            return toSrc().getLong(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default long primitiveValue() {
        assert isStatic();
        return primitiveValue(null);
    }

    @Override
    default String toString(C object) {
        return Long.toString(primitiveValue(object));
    }
}
