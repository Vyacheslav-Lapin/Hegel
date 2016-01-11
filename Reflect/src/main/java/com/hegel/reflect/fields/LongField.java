package com.hegel.reflect.fields;

@FunctionalInterface
public interface LongField<C> extends Field<C> {

    static <C> LongField<C> wrap(java.lang.reflect.Field field) {
        assert field.getType() == long.class;
        field.setAccessible(true);
        return () -> field;
    }

    default long getValue(C object) {
        try {
            return toSrc().getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default long getValue() {
        assert isStatic();
        return getValue(null);
    }

    @Override
    default String toString(C object) {
        return Long.toString(getValue(object));
    }
}
