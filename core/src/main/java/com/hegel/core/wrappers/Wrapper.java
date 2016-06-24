package com.hegel.core.wrappers;

/**
 * Interface to make proxy classes and interfaces.
 * @param <T>
 */
@FunctionalInterface
public interface Wrapper<T> {

    T toSrc();

//    static <T> Wrapper<T> wrap(T t) {
//        return () -> t;
//    }
}
