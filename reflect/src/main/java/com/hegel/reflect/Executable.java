package com.hegel.reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @param <T> Type of Method returned value. Equals to C for Constructor
 * @param <C> Class owns the Method or Constructor
 * @param <E> Source of base reflect Java package, wrapped by this Interface
 */
public interface Executable<T, C, E extends java.lang.reflect.Executable> extends Member<T, C, E> {

    T execute(C thisObject, Object... params) throws InvocationTargetException, IllegalAccessException;

    default T execute(Object... params) throws InvocationTargetException, IllegalAccessException {
        assert isStatic();
        return execute(null, params);
    }

    default Stream<Parameter<?, Executable<T, C, E>>> parameters() {
        return Stream.of(toSrc().getParameters()).map(Parameter::wrap);
    }

    default Stream<Class<?>> paramTypes() {
        return Arrays.stream(toSrc().getParameterTypes()).map(Class::wrap);
    }

    // TODO: 3/22/2016 Move this method to Util-interface com.hegel.core.Streams
    default boolean paramTypesCheck(Object[] params) {
        return Arrays.equals(
                Arrays.stream(toSrc().getParameterTypes())
                        .map(BaseType::from)
                        .toArray(java.lang.Class[]::new),
                Arrays.stream(params)
                        .map(Object::getClass)
                        .map(BaseType::from)
                        .toArray(java.lang.Class[]::new));
    }

    default int getParameterCount() {
        return toSrc().getParameterCount();
    }
}
