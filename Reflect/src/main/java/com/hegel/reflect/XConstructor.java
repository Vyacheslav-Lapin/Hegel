package com.hegel.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

public class XConstructor<C> {

    private Constructor<C> constructor;

    private XConstructor(Constructor<C> constructor) {
        this.constructor = constructor;
    }

    public Class<C> getDeclaringClass() {
        return constructor.getDeclaringClass();
    }

    public String getName() {
        return constructor.getName();
    }

    public Stream<TypeVariable<?>> getTypeParameters() {
        return Stream.of(constructor.getTypeParameters());
    }

    public Stream<Class<?>> getParameterTypes() {
        return Stream.of(constructor.getParameterTypes());
    }

    public Class<?>[] getExceptionTypes() {
        return constructor.getExceptionTypes();
    }

    public String toGenericString() {
        return constructor.toGenericString();
    }

    public Annotation[][] getParameterAnnotations() {
        return constructor.getParameterAnnotations();
    }

    public AnnotatedType getAnnotatedReturnType() {
        return constructor.getAnnotatedReturnType();
    }

    public static <C> XConstructor<C> from(Constructor<C> constructor) {
        return new XConstructor<>(constructor);
    }
}
