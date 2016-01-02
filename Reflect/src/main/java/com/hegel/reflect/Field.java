package com.hegel.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public class Field<F, C> //extends AccessibleObject implements Member
{
    private java.lang.reflect.Field field;
    private int modifiers;

    @SuppressWarnings("unchecked")
    protected Field(java.lang.reflect.Field field) {
        this(field, (java.lang.Class<F>) field.getType());
    }

    @SuppressWarnings("unchecked")
    protected Field(java.lang.reflect.Field field, java.lang.Class<F> type) {
//        assert field.getType().equals(type);
        this(field, Class.wrap(type), Class.wrap((java.lang.Class<C>) field.getDeclaringClass()));
    }

    protected Field(java.lang.reflect.Field field, Class<F> type, Class<C> declaringClass) {

        assert field.getType().equals(type.toSrc());
        assert field.getDeclaringClass().equals(declaringClass.toSrc());

        field.setAccessible(true);
        this.field = field;
        modifiers = field.getModifiers();
    }

    public static <F, C> Field<F, C> wrap(java.lang.reflect.Field field, Class<F> type, Class<C> declaringClass) {
        assert field.getType().equals(type.toSrc());
        assert field.getDeclaringClass().equals(declaringClass.toSrc());

        return new Field<>(field, type, declaringClass);
    }

    public static <F, C> Field<F, C> wrap(String name, Class<C> declaringClass) {
        return declaringClass.getField(name);
    }

    public String toString(C object) {
        try {
            return field.get(object).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass())
                && field.equals(((Field<?, ?>) o).field);

    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @SuppressWarnings("unchecked")
    public static <F, C> Field<F, C> wrap(java.lang.reflect.Field field) {
        return new Field<>(field,
                Class.wrap((java.lang.Class<F>) field.getType()),
                Class.wrap((java.lang.Class<C>) field.getDeclaringClass()));
    }

    public java.lang.reflect.Field getSrc() {
        return field;
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    public boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }

    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    public boolean isPrimitive() {
        return field.getType().isPrimitive();
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    public boolean isVolatile() {
        return Modifier.isVolatile(modifiers);
    }

    public boolean isPackagePrivate() {
        return !Modifier.isPrivate(modifiers)
                && !Modifier.isProtected(modifiers)
                && !Modifier.isPublic(modifiers);
    }

    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }

    @SuppressWarnings("unchecked")
    public F getValue(C object) {
        Object o;
        try {
            o = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (F) o;
    }

    @SuppressWarnings("unchecked")
    public java.lang.Class<F> getType() {
        return (java.lang.Class<F>) field.getType();
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
