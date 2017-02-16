package com.hegel.reflect.fields;

@FunctionalInterface
public interface ObjectField<F, C> extends Field<C> {

    static <F, C> ObjectField<F, C> wrap(java.lang.reflect.Field field) {
        field.setAccessible(true);
        return () -> field;
    }

    @SuppressWarnings("unchecked")
    default F getValue(C object) {
        try {
            return (F) toSrc().get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default F getValue() {
        assert isStatic();
        return getValue(null);
    }
}
