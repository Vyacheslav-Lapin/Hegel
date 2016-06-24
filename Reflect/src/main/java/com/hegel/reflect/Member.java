package com.hegel.reflect;

import com.hegel.core.wrappers.Wrapper;

import java.lang.reflect.Modifier;

/**
 * @param <T> Type of field or of Method returned value. Equals to C for Constructor
 * @param <C> Class owns the Field, Method or Constructor
 * @param <M> Source class
 */
public interface Member<T, C, M extends java.lang.reflect.Member> extends Wrapper<M> {

    default boolean isPrimitive() {
        return false;
    }

    Class<T> getType();

    @SuppressWarnings("unchecked")
    default Class<C> getOwnerClass() {
        return Class.wrap((java.lang.Class<C>) toSrc().getDeclaringClass());
    }

    default int getModifiers() {
        return toSrc().getModifiers();
    }

    default boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    default boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    default boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    default boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    default boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    default boolean isPackagePrivate() {
        return !Modifier.isPrivate(getModifiers())
                && !Modifier.isProtected(getModifiers())
                && !Modifier.isPublic(getModifiers());
    }

    default boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    default String getName() {
        return toSrc().getName();
    }
}
