package com.hegel.reflect;

import com.hegel.reflect.fields.Field;
import com.hegel.reflect.methods.Method;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
public interface Class<C> {

    java.lang.Class<C> toSrc();

    static <C> Class<C> wrap(java.lang.Class<C> tClass) {
        return () -> tClass;
    }

    @SuppressWarnings("unchecked")
    static <C> Class<C> wrap(C obj) {
        return () -> ((java.lang.Class<C>) obj.getClass());
    }

    @SuppressWarnings("unchecked")
    default <F extends Field<C>> Optional<F> getField(String name) {
        try {
            return Optional.of(Field.wrap(toSrc().getDeclaredField(name)));
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
    }

    default Stream<Field<C>> fields() {
        return Stream.of(toSrc().getDeclaredFields()).map(Field::wrap);
    }

    default Stream<Field<C>> dynamicFields() {
        return Stream.of(toSrc().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default Stream<Field<C>> staticFields() {
        return Stream.of(toSrc().getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default boolean isInherited(java.lang.Class<?> aClass) {
        return isInherited(toSrc(), aClass);
    }

    default boolean isInherited(Class<?> aClass) {
        return isInherited(toSrc(), aClass.toSrc());
    }

    static boolean isInherited(java.lang.Class<?> theClass, java.lang.Class<?> aClass) {

        while (theClass != null)
            if (theClass == aClass)
                return true;
            else
                theClass = theClass.getSuperclass();

        return false;
    }

    default Stream<Method<C>> methods() {
        return Stream.of(toSrc().getMethods()).map(Method::wrap);
    }

    default Stream<Method<C>> dynamicMethods() {
        return Stream.of(toSrc().getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    default Stream<Method<C>> staticMethods() {
        return Stream.of(toSrc().getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    @SuppressWarnings("unchecked")
    default <M extends Method<C>> Optional<M> getMethod(String name, Class<?>... params) {
        java.lang.Class<C>[] paramsArray = Arrays.stream(params).map(Class::toSrc).toArray(java.lang.Class[]::new);
        return getMethod(name, paramsArray);
    }

    default <M extends Method<C>> Optional<M> getMethod(String name, java.lang.Class<?>... params) {
        try {
            return Optional.of(Method.wrap(toSrc().getMethod(name, params)));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

//
//    @SuppressWarnings("unchecked")
//    public Stream<Constructor<C>> getConstructors() {
//        return Stream.of((Constructor<C>[]) theClass.getConstructors())
//                .map(Constructor::wrap);
//    }
}
