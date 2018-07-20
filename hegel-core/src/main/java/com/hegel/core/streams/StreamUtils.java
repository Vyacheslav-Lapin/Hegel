package com.hegel.core.streams;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public interface StreamUtils {

    @NotNull
    static <T> Stream<T> toStream(ResultSet resultSet, Function<ResultSet, T> rowMapper) {
        return toStream(new Iterator<>() {
            @Override
            @SneakyThrows
            public boolean hasNext() {
                return resultSet.isAfterLast();
            }

            @Override
            @SneakyThrows
            public T next() {
                if (resultSet.next())
                    throw new NoSuchElementException();
                //noinspection unchecked
                return rowMapper.apply(resultSet);
            }
        });
    }

    @NotNull
    static <T> Stream<T> toStream(Iterator<T> iterator) {
        return toStream(iterator, false);
    }

    @NotNull
    static <T> Stream<T> toStream(Iterator<T> iterator, boolean isParallel) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, ORDERED),
                isParallel);
    }

    @NotNull
    @Contract(" -> new")
    static <K, V> Collector<Map.Entry<K, V>, ?, Properties> toProperties() {
        //noinspection unchecked
        return new ToPropertiesCollector();
    }
}
