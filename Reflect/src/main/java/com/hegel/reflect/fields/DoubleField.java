package com.hegel.reflect.fields;

@FunctionalInterface
public interface DoubleField<C> extends Field<Double, C> {

    static <C> DoubleField<C> wrap(java.lang.reflect.Field field) {
        assert field.getType() == double.class || field.getType() == float.class;
        field.setAccessible(true);
        return () -> field;
    }

    @Override
    default Double value(C c) {
        return primitiveValue(c);
    }

    default double primitiveValue(C object) {
        try {
            return toSrc().getDouble(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default double primitiveValue() {
        assert isStatic();
        return primitiveValue(null);
    }

    @Override
    default Double value() {
        return primitiveValue();
    }

    @Override
    default String toString(C object) {
        return Double.toString(primitiveValue(object));
    }
}
