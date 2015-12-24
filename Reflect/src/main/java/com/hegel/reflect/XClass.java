package com.hegel.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public class XClass<C> {

    private Class<C> theClass;

    public XClass(Class<C> theClass) {
        this.theClass = theClass;
    }

    public static <T> XClass<T> from(Class<T> tClass) {
        return new XClass<>(tClass);
    }

    public Class<C> toClass() {
        return theClass;
    }


    public <F> XField<F, C> getField(String name, Class<F> type) {
        try {
            return XField.from(theClass.getDeclaredField(name), type, theClass);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public <F> XField<F, C> getField(String name) {
        try {
            return getField(theClass.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public <F> XField<F, C> getField(Field field) {
        assert field.getDeclaringClass().equals(theClass);
        return XField.from(field, theClass);
    }

    public <F> StaticXField<F, C> getStaticField(String name, Class<F> type) {
        try {
            return StaticXField.from(theClass.getDeclaredField(name), type, theClass);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <F> StaticXField<F, C> getStaticField(Field field) {
        assert field.getDeclaringClass().equals(theClass);
        assert Modifier.isStatic(field.getModifiers());

        return StaticXField.from(field, (Class<F>) field.getType(), theClass);
    }

    public Stream<XField<?, C>> getFields() {
        return Stream.of(theClass.getFields()).map(this::getField);
    }

    public Stream<XField<?, C>> getDinamicFields() {
        return Stream.of(theClass.getFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(this::getField);
    }

    public Stream<StaticXField<?, C>> getStaticFields() {
        return Stream.of(theClass.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(this::getStaticField);
    }


    public Stream<XMethod<?, C>> getMethods() {
        return Stream.of(theClass.getMethods())
                .map(this::getMethod);
    }

    @SuppressWarnings("unchecked")
    private <R> XMethod<R, C> getMethod(Method method) {
        return XMethod.from(method, (Class<R>) method.getReturnType(), theClass);
    }

    @SuppressWarnings("unchecked")
    public Stream<XConstructor<C>> getConstructors() {
        return Stream.of((Constructor<C>[]) theClass.getConstructors())
                .map(XConstructor::from);
    }
}
