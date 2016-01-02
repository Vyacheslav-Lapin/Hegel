package com.hegel.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public class Class<C> {

    private java.lang.Class<C> theClass;

    protected Class(java.lang.Class<C> theClass) {
        this.theClass = theClass;
    }

    public static <C> Class<C> wrap(java.lang.Class<C> tClass) {
        return new Class<>(tClass);
    }

    @SuppressWarnings("unchecked")
    public static <C> Class<C> wrap(C obj) {
        return new Class<>((java.lang.Class<C>) obj.getClass());
    }

    public java.lang.Class<C> toSrc() {
        return theClass;
    }


    public <F> Field<F, C> getField(String name) {
        try {
            java.lang.reflect.Field declaredField = theClass.getDeclaredField(name);
            //noinspection unchecked
            return Field.wrap(declaredField, wrap((java.lang.Class<F>) declaredField.getType()), this);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
//
//    public <F> Field<F, C> getField(String name) {
//        try {
//            return getField(theClass.getDeclaredField(name));
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public <F> Field<F, C> getField(java.lang.reflect.Field field) {
//        assert field.getDeclaringClass().equals(theClass);
//        return Field.wrap(field, theClass);
//    }
//
//    public <F> StaticField<F, C> getStaticField(String name, java.lang.Class<F> type) {
//        try {
//            return StaticField.wrap(theClass.getDeclaredField(name), type, theClass);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public <F> StaticField<F, C> getStaticField(java.lang.reflect.Field field) {
//        assert field.getDeclaringClass().equals(theClass);
//        assert Modifier.isStatic(field.getModifiers());
//
//        return StaticField.wrap(field, (java.lang.Class<F>) field.getType(), theClass);
//    }
//
//    public Stream<Field<?, C>> getFields() {
//        return Stream.of(theClass.getFields()).map(this::getField);
//    }
//
//    public Stream<Field<?, C>> getDinamicFields() {
//        return Stream.of(theClass.getFields())
//                .filter(field -> !Modifier.isStatic(field.getModifiers()))
//                .map(this::getField);
//    }
//
//    public Stream<StaticField<?, C>> getStaticFields() {
//        return Stream.of(theClass.getFields())
//                .filter(field -> Modifier.isStatic(field.getModifiers()))
//                .map(this::getStaticField);
//    }
//
//
//    public Stream<XMethod<?, C>> getMethods() {
//        return Stream.of(theClass.getMethods())
//                .map(this::getMethod);
//    }
//
//    @SuppressWarnings("unchecked")
//    private <R> XMethod<R, C> getMethod(Method method) {
//        return XMethod.wrap(method, (java.lang.Class<R>) method.getReturnType(), theClass);
//    }
//
//    @SuppressWarnings("unchecked")
//    public Stream<XConstructor<C>> getConstructors() {
//        return Stream.of((Constructor<C>[]) theClass.getConstructors())
//                .map(XConstructor::wrap);
//    }
}
