package com.hegel.reflect;

import com.hegel.reflect.fields.Field;
import com.hegel.reflect.methods.Method;
import javaslang.control.Try;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Class<C> extends Supplier<java.lang.Class<C>> {

    static <C> Class<C> wrap(java.lang.Class<C> tClass) {
        return () -> tClass;
    }

    @SuppressWarnings("unchecked")
    static <C> Class<C> wrap(C obj) {
        return () -> (java.lang.Class<C>) obj.getClass();
    }

    @SuppressWarnings("unchecked")
    default <F extends Field<C>> Try<F> getField(String name) {
        return Try.of(() -> get().getDeclaredField(name))
                .map(Field::wrap);
    }

    default Stream<Field<C>> fields() {
        return Stream.of(get().getDeclaredFields()).map(Field::wrap);
    }

    default Stream<Field<C>> dynamicFields() {
        return Stream.of(get().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default Stream<Field<C>> staticFields() {
        return Stream.of(get().getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default boolean isInherited(java.lang.Class<?> aClass) {
        return isInherited(get(), aClass);
    }

    default boolean isInherited(Class<?> aClass) {
        return isInherited(get(), aClass.get());
    }

    static boolean isInherited(java.lang.Class<?> theClass, java.lang.Class<?> aClass) {

        while (theClass != null)
            if (theClass == aClass)
                return true;
            else
                theClass = theClass.getSuperclass();

        return false;
    }

    default <M extends Method<C>> Stream<M> methods() {
        return Stream.of(get().getMethods()).map(Method::wrap);
    }

    default <M extends Method<C>> Stream<M> dynamicMethods() {
        return Stream.of(get().getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    default <M extends Method<C>> Stream<M> staticMethods() {
        return Stream.of(get().getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    @SuppressWarnings("unchecked")
    default <M extends Method<C>> Optional<M> getMethod(String name, Class<?>... params) {
        java.lang.Class<C>[] paramsArray = Arrays.stream(params).map(Class::get).toArray(java.lang.Class[]::new);
        return getMethod(name, paramsArray);
    }

    default <M extends Method<C>> Optional<M> getMethod(String name, java.lang.Class<?>... params) {
        try {
            return Optional.of(Method.wrap(get().getMethod(name, params)));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

//    @SuppressWarnings("unchecked")
//    default Stream<Constructor<C>> getConstructors() {
//        return Stream.of((Constructor<C>[]) theClass.getConstructors())
//                .map(Constructor::wrap);
//    }
}
