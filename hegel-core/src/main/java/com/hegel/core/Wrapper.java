package com.hegel.core;

import java.util.function.Supplier;

@FunctionalInterface
public interface Wrapper<T> extends Supplier<T> {
    @Private
    @Deprecated
    default T toSrc() {
        return get();
    }

    @Override
    T get();
}
