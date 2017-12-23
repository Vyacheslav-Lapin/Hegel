package com.hegel.core.functions;

import java.util.function.Consumer;

@FunctionalInterface
public interface VarConsumer<T> extends Consumer<T[]> {

    @Override
    @SuppressWarnings("unchecked")
    void accept(T... params);

    default Runnable toRunnable(T... params) {
        return () -> accept(params);
    }
}
