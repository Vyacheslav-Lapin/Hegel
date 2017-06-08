package com.hegel.core.functions;

import com.hegel.core.Either;
import javaslang.control.Option;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Exceptional<T, E extends Throwable> extends Supplier<Either<T, E>>/*, javaslang.control.Try */ {

    static <T, E extends Throwable> Exceptional<T, E> wrap(Either<T, E> either) {
        return () -> either;
    }

    @SuppressWarnings("unused")
    default <T1> Exceptional<T1, E> mapValue(Function<T, T1> valueTransformer) {
        return wrap(get().mapLeft(valueTransformer));
    }

    default <E1 extends Throwable> Exceptional<T, E1> mapException(Function<E, E1> exceptionTransformer) {
        return wrap(get().mapRight(exceptionTransformer));
    }

    default <T1, E1 extends Throwable> Exceptional<T1, E1> map(Function<T, T1> valueTransformer,
                                                               Function<E, E1> exceptionTransformer) {
        return wrap(get().map(valueTransformer, exceptionTransformer));
    }

    default T getOrThrow() throws E {
        final Either<T, E> either = get();
        if (either.isLeft()) {
            return either.left();
        } else {
            throw either.right();
        }
    }

    default <E1 extends Throwable> T getOrThrowUnchecked() throws E1 {
        return getOrThrow(RuntimeException::new);
    }
    
    default <E1 extends Throwable> T getOrThrow(Function<E, E1> exceptionMapper) throws E1 {
        return mapException(exceptionMapper).getOrThrow();
    }

    static <T, E extends Throwable> Exceptional<T, E> withValue(T value) {
        Either<T, E> left = Either.left(value);
        return () -> left;
    }

    static <T, E extends Throwable> Exceptional<T, E> withException(E exception) {
        Either<T, E> right = Either.right(exception);
        return () -> right;
    }

    default Option<T> toOption() {
        return Option.of(get().left());
    }

    default Optional<T> toOptional() {
        return Optional.ofNullable(get().left());
    }
}
