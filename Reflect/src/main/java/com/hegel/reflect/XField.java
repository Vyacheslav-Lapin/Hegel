package com.hegel.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public class XField<F, C> extends AccessibleObject implements Member {

    private Field field;
    private int modifiers;

    @SuppressWarnings("unchecked")
    public XField(Field field) {
        this(field, (Class<F>) field.getType());
    }

    @SuppressWarnings("unchecked")
    public XField(Field field, Class<F> type) {
//        assert field.getType().equals(type);
        this(field, type, (Class<C>) field.getDeclaringClass());
    }

    public XField(Field field, Class<F> type, Class<C> declaringClass) {

        assert field.getType().equals(type);
        assert field.getDeclaringClass().equals(declaringClass);

        this.field = field;
        modifiers = field.getModifiers();
    }

    public static <F, C> XField<F, C> from(Field field, Class<F> type, Class<C> declaringClass) {
        assert field.getType().equals(type);
        assert field.getDeclaringClass().equals(declaringClass);

        return new XField<>(field, type, declaringClass);
    }

    @SuppressWarnings("unchecked")
    public static <F, C> XField<F, C> from(Field field, Class<C> declaringClass) {
        assert field.getDeclaringClass().equals(declaringClass);
        return new XField<>(field, (Class<F>) field.getType(), declaringClass);
    }

    @SuppressWarnings("unchecked")
    public static <F, C> XField<F, C> from(Field field) {
        return new XField<>(field, (Class<F>) field.getType(), (Class<C>) field.getDeclaringClass());
    }

    public Field getField() {
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

    @SuppressWarnings("unchecked")
    public F getValue(C object) {
        Object o;
        try {
            o = field.get(object);
        } catch (IllegalAccessException e) {
            field.setAccessible(true);
            try {
                o = field.get(object);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException(e);
            }
        }
        return (F) o;
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    @SuppressWarnings("unchecked")
    public Class<F> getType() {
        return (Class<F>) field.getType();
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

    static <T> void parseSet(String value, T t, Field field) {
        Class<?> type = field.getType();
        try {
            if (type == String.class) field.set(t, value);

            if (type == int.class) field.setInt(t, Integer.parseInt(value));
            if (type == Integer.class) field.set(t, Integer.parseInt(value));

            if (type == boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?
            if (type == Boolean.class) field.setBoolean(t, Boolean.parseBoolean(value)); // todo: 1, y, Y, yes?

            if (type == double.class) field.setDouble(t, Double.parseDouble(value));
            if (type == Double.class) field.set(t, Double.parseDouble(value));

            if (type == long.class) field.setLong(t, Long.parseLong(value));
            if (type == Long.class) field.set(t, Long.parseLong(value));

            if (type == char.class) field.setChar(t, (char) Integer.parseInt(value));
            if (type == Character.class) field.set(t, (char) Integer.parseInt(value));

            if (type == float.class) field.setFloat(t, Float.parseFloat(value));
            if (type == Float.class) field.set(t, Float.parseFloat(value));

            if (type == short.class) field.setShort(t, Short.parseShort(value));
            if (type == Short.class) field.set(t, Short.parseShort(value));

            if (type == byte.class) field.setShort(t, Byte.parseByte(value));
            if (type == Byte.class) field.set(t, Byte.parseByte(value));

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public boolean isSynthetic() {
        return field.isSynthetic();
    }
}
