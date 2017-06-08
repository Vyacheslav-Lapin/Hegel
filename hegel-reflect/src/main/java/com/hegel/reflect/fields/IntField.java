package com.hegel.reflect.fields;

@FunctionalInterface
public interface IntField<C> extends Field<C> {

    static <C> IntField<C> wrap(java.lang.reflect.Field field) {
        assert field.getType() == int.class || field.getType() == short.class || field.getType() == char.class || field.getType() == byte.class;
        field.setAccessible(true);
        return () -> field;
    }

    default int getValue(C object) {
        try {
            return get().getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default int getValue() {
        assert isStatic();
        return getValue(null);
    }

    @Override
    default String toString(C object) {
        return Integer.toString(getValue(object));
    }
}
