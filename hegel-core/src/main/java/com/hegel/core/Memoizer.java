package com.hegel.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoizer<T, U> {
    private final Map<T, U> memoizationCache = new ConcurrentHashMap<>();

    public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
        return new Memoizer<T, U>().doMemoize(function);
    }

    private Function<T, U> doMemoize(final Function<T, U> function) {
        return input -> memoizationCache.computeIfAbsent(input, function);
    }
}
