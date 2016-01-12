package com.hegel.reflect.fields;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;

import java.lang.reflect.Modifier;
import java.util.Optional;

@FunctionalInterface
public interface Field<C> {

    java.lang.reflect.Field toSrc();

    default BaseType getType() {
        return BaseType.from(toSrc().getType());
    }

    default int getModifiers() {
        return toSrc().getModifiers();
    }

    @SuppressWarnings("unchecked")
    static <C, F extends Field<C>> F wrap(java.lang.reflect.Field field) {
        java.lang.Class<?> type = field.getType();
        return (F) (type == int.class || type == short.class || type == char.class || type == byte.class ? IntField.wrap(field) :
                type == long.class ? LongField.wrap(field) :
                        type == double.class || type == float.class ? DoubleField.wrap(field) :
                                ObjectField.wrap(field));
    }

    static <C> Optional<Field<C>> wrap(String name, Class<C> declaringClass) {
        return declaringClass.getField(name);
    }

    default String toString(C object) {
        try {
            return toSrc().get(object).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
