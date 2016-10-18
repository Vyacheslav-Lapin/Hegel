package com.hegel.core;

@FunctionalInterface
public interface Wrapper<T> {
    @Private
    T toSrc();
}
