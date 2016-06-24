package com.hegel.reflect;

import com.hegel.core.wrappers.Wrapper;
import com.hegel.reflect.fields.Field;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.hegel.reflect.BaseType.from;

@FunctionalInterface
public interface Class<C> extends Wrapper<java.lang.Class<C>> {

    static <C> Class<C> wrap(java.lang.Class<C> aClass) {
        return () -> aClass;
    }

    @SuppressWarnings("unchecked")
    static <C> Class<C> wrap(C obj) {
        return () -> ((java.lang.Class<C>) obj.getClass());
    }

    default boolean isPrimitive() {
        return from(this) != BaseType.REFERENCE;
    }

    default Class<?> boxType() {
        return wrap(BaseType.boxType(toSrc()));
    }

    @SuppressWarnings("unchecked")
    default <T, F extends Field<T, C>> Optional<F> getField(String name) {
        try {
            return Optional.of(Field.wrap(toSrc().getDeclaredField(name)));
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
    }

    default Stream<Field<?, C>> fields() {
        return Stream.of(toSrc().getDeclaredFields()).map(Field::wrap);
    }

    default Stream<Field<?, C>> dynamicFields() {
        return Stream.of(toSrc().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::wrap);
    }

    default Stream<Field<?, C>> staticFields() {
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

    @SuppressWarnings("unchecked")
    default <T> Stream<Method<T, C>> methods() {
        return Stream.of(toSrc().getMethods()).map(Method::wrap);
    }

    @SuppressWarnings("unchecked")
    default <T> Stream<Method<T, C>> dynamicMethods() {
        return Stream.of(toSrc().getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    @SuppressWarnings("unchecked")
    default <T> Stream<Method<T, C>> staticMethods() {
        return Stream.of(toSrc().getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .map(Method::wrap);
    }

    @SuppressWarnings("unchecked")
    default <T> Optional<Method<T, C>> getMethod(String name, Class<?>... params) {
        java.lang.Class<C>[] paramsArray = Arrays.stream(params).map(Class::toSrc).toArray(java.lang.Class[]::new);
        return getMethod(name, paramsArray);
    }

    default <T> Optional<Method<T, C>> getMethod(String name, java.lang.Class<?>... params) {
        try {
            return Optional.of(Method.wrap(toSrc().getMethod(name, params)));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    default Stream<Constructor<C>> constructors() {
        return Stream.of(toSrc().getConstructors()).map(Constructor::wrap);
    }

    default String getName() {
        return toSrc().getSimpleName();
    }

    default Optional<Constructor<C>> findConstructorByParamNames(Set<String> paramNames) {
        return constructors()
                .filter(tConstructor -> tConstructor.parameters()
                        .map(Parameter::getName)
                        .allMatch(paramNames::contains))
                .max(Comparator.comparing(Constructor::getParameterCount)); // TODO: find better criteria
    }
}
