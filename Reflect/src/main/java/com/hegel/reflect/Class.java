package com.hegel.reflect;

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

    default Optional<Field<C>> getField(String name) {
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
        return "select " + dinamicFields().map(Field::toSqlName).collect(Collectors.joining(", ")) + " from " + toSrc().getSimpleName();
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
