package com.hegel.core.functions;

@FunctionalInterface
public interface VarConsumer<T> {

    void accept(T... t);
}
