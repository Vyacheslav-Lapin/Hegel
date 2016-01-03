package com.hegel.reflect;

import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

public interface Class<C> {

    java.lang.Class<C> toSrc();

    static <C> Class<C> wrap(java.lang.Class<C> tClass) {
        return () -> tClass;
    }

    @SuppressWarnings("unchecked")
    static <C> Class<C> wrap(C obj) {
        return () -> ((java.lang.Class<C>) obj.getClass());
    }

    default <F> Optional<Field<F, C>> getField(String name) {
        try {
            return Optional.of(Field.wrap(toSrc().getDeclaredField(name)));
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
    }

    default Stream<Field<?, C>> fields() {
        return Stream.of(toSrc().getFields()).map(Field::wrap);
    }

    default Stream<Field<?, C>> getDinamicFields() {
        return Stream.of(toSrc().getFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default Stream<Field<?, C>> getStaticFields() {
        return Stream.of(toSrc().getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
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
