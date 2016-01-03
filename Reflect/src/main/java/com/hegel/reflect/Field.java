package com.hegel.reflect;

import java.lang.reflect.Modifier;
import java.util.Optional;

public interface Field<F, C> {

    java.lang.reflect.Field toSrc();

    default int getModifiers() {
        return toSrc().getModifiers();
    }

    static <F, C> Field<F, C> wrap(java.lang.reflect.Field field, Class<F> type, Class<C> declaringClass) {
        assert field.getType().equals(type.toSrc());
        assert field.getDeclaringClass().equals(declaringClass.toSrc());

        field.setAccessible(true);
        return () -> field;
    }

    static <F, C> Optional<Field<F, C>> wrap(String name, Class<C> declaringClass) {
        return declaringClass.getField(name);
    }

    default String toString(C object) {
        try {
            return toSrc().get(object).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <F, C> Field<F, C> wrap(java.lang.reflect.Field field) {
        return Field.wrap(field,
                Class.wrap((java.lang.Class<F>) field.getType()),
                Class.wrap((java.lang.Class<C>) field.getDeclaringClass()));
    }

    default boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    default boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    default boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    default boolean isPrimitive() {
        return toSrc().getType().isPrimitive();
    }

    default boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    default boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    default boolean isPackagePrivate() {
        return !Modifier.isPrivate(getModifiers())
                && !Modifier.isProtected(getModifiers())
                && !Modifier.isPublic(getModifiers());
    }

    default boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @SuppressWarnings("unchecked")
    default F getValue(C object) {
        Object o;
        try {
            o = toSrc().get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (F) o;
    }

    @SuppressWarnings("unchecked")
    default java.lang.Class<F> getType() {
        return (java.lang.Class<F>) toSrc().getType();
    }

//    static <T> void parseSet(String value, T t, java.lang.reflect.Field field) {
//        Class<?> type = field.getType();
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
