package com.hegel.core.functions;

import java.util.Arrays;
import java.util.function.Function;

@FunctionalInterface
public interface ExceptionalRunnable<E extends Exception> extends Runnable {

    @SafeVarargs
    static <E extends Exception> void run(ExceptionalRunnable<E>... exceptionalRunnables) {
        Arrays.stream(exceptionalRunnables)
                .forEach(exceptionalRunnable -> run(exceptionalRunnable, RuntimeException::new));
    }

    static <E extends Exception, E1 extends Exception> void run(ExceptionalRunnable<E> exceptionalRunnable,
                                                                Function<E, E1> exceptionTransformer) throws E1 {
        try {
            exceptionalRunnable.call();
        } catch (Exception e) {
            //noinspection unchecked
            throw exceptionTransformer.apply((E) e);
        }
    }

    void call() throws E;

    @Override
    default void run() {
        try {
            call();
        } catch (Exception e) {
            //noinspection unchecked
            ifThrowable((E) e);
        }
    }

    /**
     * Override this method if you wanna catch some of exception
     */
    default void ifThrowable(E e) {
        Exceptional.throwAsUnchecked(e);
    }
}
