package com.hegel.reflect;

import com.hegel.reflect.fields.Field;

import java.lang.reflect.Modifier;
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

    default Stream<Field<C>> dinamicFields() {
        return Stream.of(toSrc().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default Stream<Field<C>> staticFields() {
        return Stream.of(toSrc().getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default String sqlSelectQuery() {
        return "select " + dynamicFields().map(Field::toSqlName).collect(Collectors.joining(", ")) + " from " + toSrc().getSimpleName();
    default boolean isInherited(java.lang.Class<?> aClass) {
        return isInherited(toSrc(), aClass);
    }

    static boolean isInherited(java.lang.Class<?> theClass, java.lang.Class<?> aClass) {

        while (theClass != null)
            if (theClass == aClass)
                return true;
            else
                theClass = theClass.getSuperclass();

        return false;
    }


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
//    public Stream<Constructor<C>> getConstructors() {
//        return Stream.of((Constructor<C>[]) theClass.getConstructors())
//                .map(Constructor::wrap);
//    }
}
