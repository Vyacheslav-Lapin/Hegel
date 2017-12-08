package com.hegel.core.functions;

import com.hegel.core.Either;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Exceptional<T, E extends Exception> extends Supplier<Either<T, E>> {

    static <T, E extends Exception> Exceptional<T, E> wrap(Either<T, E> either) {
        return () -> either;
    }

    static <T, E extends Exception> Exceptional<T, E> withValue(T value) {
        Either<T, E> left = Either.left(value);
        return () -> left;
    }

    static <T, E extends Exception> Exceptional<T, E> withException(E exception) {
        Either<T, E> right = Either.right(exception);
        return () -> right;
    }

    static <R, E extends Exception> R throwAsUnchecked(Exception e) throws E {
        //noinspection unchecked
        throw (E) e;
    }

    @SuppressWarnings("unused")
    default <T1> Exceptional<T1, E> mapValue(Function<T, T1> valueTransformer) {
        return wrap(get().mapLeft(valueTransformer));
    }

    default <E1 extends Exception> Exceptional<T, E1> mapException(Function<E, E1> exceptionTransformer) {
        return wrap(get().mapRight(exceptionTransformer));
    }

    default <T1, E1 extends Exception> Exceptional<T1, E1> map(Function<T, T1> valueTransformer,
                                                               Function<E, E1> exceptionTransformer) {
        return wrap(get().map(valueTransformer, exceptionTransformer));
    }

    default T getOrThrow() throws E {
        Either<T, E> either = get();
        if (either.isLeft())
            return either.left();
        else
            throw either.right();
    }

    @SuppressWarnings("unused")
    default <E1 extends Exception> T getOrThrow(Function<E, E1> exceptionMapper) throws E1 {
        return mapException(exceptionMapper).getOrThrow();
    }

    default T getOrThrowUnchecked() {
        Either<T, E> either = get();
        return either.isLeft() ? either.left() : throwAsUnchecked(either.right());
    }

    default Optional<T> toOptional() {
        return Optional.ofNullable(get().left());
    }
}
