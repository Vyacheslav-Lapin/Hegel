package com.hegel.core.wrappers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

@FunctionalInterface
public interface StreamWrapper<T> extends Stream<T>, BaseStreamWrapper<T, Stream<T>> {

    static <T> StreamWrapper<T> wrap(Stream<T> stream) {
        return () -> stream;
    }

    @Override
    default Stream<T> filter(Predicate<? super T> predicate) {
        return toSrc().filter(predicate);
    }

    @Override
    default <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return toSrc().map(mapper);
    }

    @Override
    default IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return toSrc().mapToInt(mapper);
    }

    @Override
    default LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return toSrc().mapToLong(mapper);
    }

    @Override
    default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return toSrc().mapToDouble(mapper);
    }

    @Override
    default <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return toSrc().flatMap(mapper);
    }

    @Override
    default IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return toSrc().flatMapToInt(mapper);
    }

    @Override
    default LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return toSrc().flatMapToLong(mapper);
    }

    @Override
    default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return toSrc().flatMapToDouble(mapper);
    }

    @Override
    default Stream<T> distinct() {
        return toSrc().distinct();
    }

    @Override
    default Stream<T> sorted() {
        return toSrc().sorted();
    }

    @Override
    default Stream<T> sorted(Comparator<? super T> comparator) {
        return toSrc().sorted(comparator);
    }

    @Override
    default Stream<T> peek(Consumer<? super T> action) {
        return toSrc().peek(action);
    }

    @Override
    default Stream<T> limit(long maxSize) {
        return toSrc().limit(maxSize);
    }

    @Override
    default Stream<T> skip(long n) {
        return toSrc().skip(n);
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        toSrc().forEach(action);
    }

    @Override
    default void forEachOrdered(Consumer<? super T> action) {
        toSrc().forEachOrdered(action);
    }

    @Override
    default Object[] toArray() {
        return toSrc().toArray();
    }

    @Override
    default <A> A[] toArray(IntFunction<A[]> generator) {
        return toSrc().toArray(generator);
    }

    @Override
    default T reduce(T identity, BinaryOperator<T> accumulator) {
        return toSrc().reduce(identity, accumulator);
    }

    @Override
    default Optional<T> reduce(BinaryOperator<T> accumulator) {
        return toSrc().reduce(accumulator);
    }

    @Override
    default <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return toSrc().reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return toSrc().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(Collector<? super T, A, R> collector) {
        return toSrc().collect(collector);
    }

    @Override
    default Optional<T> min(Comparator<? super T> comparator) {
        return toSrc().min(comparator);
    }

    @Override
    default Optional<T> max(Comparator<? super T> comparator) {
        return toSrc().max(comparator);
    }

    @Override
    default long count() {
        return toSrc().count();
    }

    @Override
    default boolean anyMatch(Predicate<? super T> predicate) {
        return toSrc().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(Predicate<? super T> predicate) {
        return toSrc().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(Predicate<? super T> predicate) {
        return toSrc().noneMatch(predicate);
    }

    @Override
    default Optional<T> findFirst() {
        return toSrc().findFirst();
    }

    @Override
    default Optional<T> findAny() {
        return toSrc().findAny();
    }


    @Override
    default Iterator<T> iterator() {
        return BaseStreamWrapper.super.iterator();
    }

    @Override
    default Spliterator<T> spliterator() {
        return BaseStreamWrapper.super.spliterator();
    }

    @Override
    default boolean isParallel() {
        return BaseStreamWrapper.super.isParallel();
    }

    @Override
    default Stream<T> sequential() {
        return BaseStreamWrapper.super.sequential();
    }

    @Override
    default Stream<T> parallel() {
        return BaseStreamWrapper.super.parallel();
    }

    @Override
    default Stream<T> unordered() {
        return BaseStreamWrapper.super.unordered();
    }

    @Override
    default Stream<T> onClose(Runnable closeHandler) {
        return BaseStreamWrapper.super.onClose(closeHandler);
    }

    @Override
    default void close() {
        BaseStreamWrapper.super.close();
    }
}
