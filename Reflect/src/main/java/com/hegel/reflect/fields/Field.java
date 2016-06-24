package com.hegel.reflect.fields;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import com.hegel.reflect.Member;

import java.util.Optional;

public interface Field<T, C> extends Member<T, C, java.lang.reflect.Field> {

    @SuppressWarnings("unchecked")
    static <T, C, F extends Field<T, C>> F wrap(java.lang.reflect.Field field) {
        switch (BaseType.from(field.getType())) {
            case INT:
            case SHORT:
            case CHAR:
            case BYTE:
                return (F) IntField.wrap(field);
            case DOUBLE:
            case FLOAT:
                return (F) DoubleField.wrap(field);
            case LONG:
                return (F) LongField.wrap(field);
            default:
                field.setAccessible(true);
                return (F) (Field<T, C>) () -> field;
        }
    }

    static <T, C, F extends Field<T, C>> Optional<F> wrap(String name, Class<C> declaringClass) {
        return declaringClass.getField(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Class<T> getType() {
        return Class.wrap((java.lang.Class<T>) toSrc().getType());
    }

    @SuppressWarnings("unchecked")
    default T value(C c) {
        try {
            return (T) toSrc().get(c);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default T value() {
        assert isStatic();
        return value(null);
    }

    default String toString(C object) {
        try {
            return toSrc().get(object).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default boolean isPrimitive() {
        return toSrc().getType().isPrimitive();
    }

//    static <T> void parseSet(String value, T t, java.lang.reflect.Field field) {
//        Class<?> type = field.getPrimitiveClass();
//        try {
//            if (type == String.class) field.set(t, value);
//
//            if (type == int.class) field.setInt(t, Integer.parseInt(value));
//            if (type == Integer.class) field.set(t, Integer.parseInt(value));
//
//            if (type == boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?
//            if (type == Boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?
//
//            if (type == double.class) field.setDouble(t, Double.parseDouble(value));
//            if (type == Double.class) field.set(t, Double.parseDouble(value));
//
//            if (type == long.class) field.setLong(t, Long.parseLong(value));
//            if (type == Long.class) field.set(t, Long.parseLong(value));
//
//            if (type == char.class) field.setChar(t, (char) Integer.parseInt(value));
//            if (type == Character.class) field.set(t, (char) Integer.parseInt(value));
//
//            if (type == float.class) field.setFloat(t, Float.parseFloat(value));
//            if (type == Float.class) field.set(t, Float.parseFloat(value));
//
//            if (type == short.class) field.setShort(t, Short.parseShort(value));
//            if (type == Short.class) field.set(t, Short.parseShort(value));
//
//            if (type == byte.class) field.setShort(t, Byte.parseByte(value));
//            if (type == Byte.class) field.set(t, Byte.parseByte(value));
//
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
}
