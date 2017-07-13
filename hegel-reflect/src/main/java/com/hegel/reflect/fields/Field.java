package com.hegel.reflect.fields;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import io.vavr.control.Try;
import lombok.SneakyThrows;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface Field<C> extends Supplier<java.lang.reflect.Field> {

    List<java.lang.Class<?>> INT_CLASSES = Arrays.asList(int.class, short.class, char.class, byte.class);
    List<java.lang.Class<?>> DECIMAL_CLASSES = Arrays.asList(double.class, float.class);

    @SuppressWarnings("unchecked")
    static <C, F extends Field<C>> F wrap(java.lang.reflect.Field field) {
        java.lang.Class<?> type = field.getType();
        return (F) (
                INT_CLASSES.contains(type) ? IntField.wrap(field) :
                        DECIMAL_CLASSES.contains(type) ? DoubleField.wrap(field) :
                                type == long.class ? LongField.wrap(field) :
                                        ObjectField.wrap(field));
    }

    static <C> Try<Field<C>> wrap(String name, Class<C> declaringClass) {
        return declaringClass.getField(name);
    }

    default BaseType getType() {
        return BaseType.from(Class.wrap(get().getType()));
    }

    default Class<?> getOwnerClass() {
        return Class.wrap(get().getDeclaringClass());
    }

    default int getModifiers() {
        return get().getModifiers();
    }

    @SneakyThrows
    default String toString(C object) {
        return get().get(object).toString();
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
        return get().getType().isPrimitive();
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

//    @SneakyThrows
//    static <T> void parseSet(String value, T t, java.lang.reflect.Field field) {
//        java.lang.Class<?> type = field.getType();
//        if (type == String.class) field.set(t, value);
//
//        if (type == int.class) field.setInt(t, Integer.parseInt(value));
//        if (type == Integer.class) field.set(t, Integer.parseInt(value));
//
//        if (type == boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?
//        if (type == Boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?
//
//        if (type == double.class) field.setDouble(t, Double.parseDouble(value));
//        if (type == Double.class) field.set(t, Double.parseDouble(value));
//
//        if (type == long.class) field.setLong(t, Long.parseLong(value));
//        if (type == Long.class) field.set(t, Long.parseLong(value));
//
//        if (type == char.class) field.setChar(t, (char) Integer.parseInt(value));
//        if (type == Character.class) field.set(t, (char) Integer.parseInt(value));
//
//        if (type == float.class) field.setFloat(t, Float.parseFloat(value));
//        if (type == Float.class) field.set(t, Float.parseFloat(value));
//
//        if (type == short.class) field.setShort(t, Short.parseShort(value));
//        if (type == Short.class) field.set(t, Short.parseShort(value));
//
//        if (type == byte.class) field.setShort(t, Byte.parseByte(value));
//        if (type == Byte.class) field.set(t, Byte.parseByte(value));
//    }

}
