package com.hegel.reflect.fields;

@FunctionalInterface
public interface IntField<C> extends Field<Integer, C> {

    static <C> IntField<C> wrap(java.lang.reflect.Field field) {

        assert field.getType() == int.class
                || field.getType() == short.class
                || field.getType() == char.class
                || field.getType() == byte.class;

        field.setAccessible(true);

        return () -> field;
    }

    @Override
    default Integer value(C c) {
        return primitiveValue(c);
    }

    default int primitiveValue(C object) {
        try {
            return toSrc().getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default int primitiveValue() {
        assert isStatic();
        return primitiveValue(null);
    }

    @Override
    default String toString(C object) {
        return Integer.toString(primitiveValue(object));
    }
}
