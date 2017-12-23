package com.hegel.core.functions;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface VarFunction<T, R> extends Function<T[], R> {

    static <T, R> Supplier<R> supply(VarFunction<T, R> varFunction, T... params) {
        return varFunction.supply(params);
    }

    static <T, R> VarFunction<T, R> partialLeft(VarFunction<T, R> varFunction, T... params) {
        return varFunction.partialLeft(params);
    }

    static <T, R> VarFunction<T, R> partialRight(VarFunction<T, R> varFunction, T... params) {
        return varFunction.partialRight(params);
    }

    @Override
    @SuppressWarnings("unchecked")
    R apply(T... params);

    @SuppressWarnings("unchecked")
    default Supplier<R> supply(T... params) {
        return () -> apply(params);
    }

    @SuppressWarnings("unchecked")
    default VarFunction<T, R> partialLeft(T... params) {
        return additionalParams -> {
            T[] result = Arrays.copyOf(params, params.length + additionalParams.length);
            System.arraycopy(additionalParams, 0, result, params.length, additionalParams.length);
            return apply(result);
        };
    }

    @SuppressWarnings("unchecked")
    default VarFunction<T, R> partialRight(T... params) {
        return additionalParams -> {
            T[] result = Arrays.copyOf(additionalParams, params.length + additionalParams.length);
            System.arraycopy(params, 0, result, additionalParams.length, params.length);
            return apply(result);
        };
    }
}
