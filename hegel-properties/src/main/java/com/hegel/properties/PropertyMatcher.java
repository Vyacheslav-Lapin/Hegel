package com.hegel.properties;

import com.hegel.core.functions.ExceptionalBiConsumer;
import com.hegel.core.functions.ExceptionalBiFunction;
import com.hegel.core.functions.ExceptionalConsumer;
import com.hegel.core.functions.ExceptionalFunction;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface PropertyMatcher extends Supplier<Properties> {

    @SneakyThrows
    static PropertyMatcher from(Supplier<InputStream> inputStreamSupplier) {
        try (InputStream inputStream = inputStreamSupplier.get()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return () -> properties;
        }
    }

    default PropertyMatcher ensure(Predicate<PropertyMatcher> test) {
        if (test.test(this))
            return this;
        throw new RuntimeException("Test failed!");
    }

    default PropertyMatcher ensure(BooleanSupplier test) {
        if (test.getAsBoolean())
            return this;
        throw new RuntimeException("Test failed!");
    }

    default PropertyMatcher ensureKeysExist(String... keys) {
        Properties properties = get();
        return ensure(() -> {
            for (String key: keys)
                if(!properties.containsKey(key))
                    return false;
            return true;
        });
    }

    default PropertyMatcher ensureOnlyThatKeysExists(String... keys) {
        ensure(() -> keys.length == get().size());
        return ensureKeysExist(keys);
    }

    default <E extends Exception> PropertyMatcher with(String key,
                                                       ExceptionalConsumer<Optional<String>, E> consumer) {
        consumer.accept(
                Optional.ofNullable(
                        (String) get().remove(key)));

        return this;
    }

    default <E extends Exception> PropertyMatcher with(String key,
                                                       ExceptionalBiConsumer<Optional<String>, PropertyMatcher, E> consumer) {
        consumer.accept(
                Optional.ofNullable(
                        (String) get().remove(key)),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher with(String key,
                                                       String defaultValue,
                                                       ExceptionalConsumer<String, E> consumer) {
        Object value = get().remove(key);
        consumer.accept(
                value == null ? defaultValue : (String) value);

        return this;
    }

    default <E extends Exception> PropertyMatcher with(String key,
                                                       String defaultValue,
                                                       ExceptionalBiConsumer<String, PropertyMatcher, E> consumer) {
        Object value = get().remove(key);
        consumer.accept(
                value == null ? defaultValue : (String) value,
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withInt(String key,
                                                          ExceptionalConsumer<Optional<Integer>, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Integer::valueOf, (String) get().remove(key)
                ).getAsOptional());

        return this;
    }

    default <E extends Exception> PropertyMatcher withInt(String key,
                                                          ExceptionalBiConsumer<Optional<Integer>, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Integer::valueOf, (String) get().remove(key)
                ).getAsOptional(),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withInt(String key,
                                                          int defaultValue,
                                                          ExceptionalConsumer<Integer, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Integer::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue));

        return this;
    }

    default <E extends Exception> PropertyMatcher withInt(String key,
                                                          int defaultValue,
                                                          ExceptionalBiConsumer<Integer, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Integer::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withDouble(String key,
                                                             ExceptionalConsumer<Optional<Double>, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key))
                        .getAsOptional());

        return this;
    }

    default <E extends Exception> PropertyMatcher withDouble(String key,
                                                             ExceptionalBiConsumer<Optional<Double>, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key))
                        .getAsOptional(),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withDouble(String key,
                                                             double defaultValue,
                                                             ExceptionalConsumer<Double, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue));

        return this;
    }

    default <E extends Exception> PropertyMatcher withDouble(String key,
                                                             double defaultValue,
                                                             ExceptionalBiConsumer<Double, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withLong(String key,
                                                           ExceptionalConsumer<Optional<Long>, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key))
                        .getAsOptional());

        return this;
    }

    default <E extends Exception> PropertyMatcher withLong(String key,
                                                           ExceptionalBiConsumer<Optional<Long>, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key))
                        .getAsOptional(),
                this);

        return this;
    }

    default <E extends Exception> PropertyMatcher withLong(String key,
                                                           long defaultValue,
                                                           ExceptionalConsumer<Long, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue));

        return this;
    }

    default <E extends Exception> PropertyMatcher withLong(String key,
                                                           long defaultValue,
                                                           ExceptionalBiConsumer<Long, PropertyMatcher, E> consumer) {
        consumer.accept(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key))
                        .getAsOptional()
                        .orElse(defaultValue),
                this);

        return this;
    }

    default <T, E1 extends Exception, E2 extends Exception> PropertyMatcher withTransform(
            String key,
            ExceptionalFunction<String, T, E1> valueMapper,
            ExceptionalConsumer<Optional<T>, E2> valueConsumer) {

        valueConsumer.accept(
                valueMapper.apply(
                        (String) get().remove(key)).toOptional());

        return this;
    }

    default <T, E1 extends Exception, E2 extends Exception> PropertyMatcher withTransform(
            String key,
            ExceptionalFunction<String, T, E1> valueMapper,
            ExceptionalBiConsumer<Optional<T>, PropertyMatcher, E2> valueConsumer) {

        valueConsumer.accept(
                valueMapper.apply(
                        (String) get().remove(key))
                        .toOptional(),
                this);

        return this;
    }

    default <T, E1 extends Exception, E2 extends Exception> PropertyMatcher withTransform(
            String key,
            String defaultValue,
            ExceptionalFunction<String, T, E1> valueMapper,
            ExceptionalConsumer<T, E2> valueConsumer) {

        Object value = get().remove(key);
        valueConsumer.accept(
                valueMapper.apply(
                        value == null ? defaultValue : (String) value)
                        .getOrThrowUnchecked());

        return this;
    }

    default <T, E1 extends Exception, E2 extends Exception> PropertyMatcher withTransform(
            String key,
            String defaultValue,
            ExceptionalFunction<String, T, E1> valueMapper,
            ExceptionalBiConsumer<T, PropertyMatcher, E2> valueConsumer) {

        Object value = get().remove(key);
        valueConsumer.accept(
                valueMapper.apply(
                        value == null ? defaultValue : (String) value)
                        .getOrThrowUnchecked(),
                this);

        return this;
    }

    default <T, E extends Exception> T map(String key, ExceptionalFunction<Optional<String>, T, E> mapper) {
        return mapper.apply(
                Optional.ofNullable(
                        (String) get().remove(key)))
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T map(String key,
                                           String defaultValue,
                                           ExceptionalFunction<String, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue : (String) value)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T map(String key,
                                           ExceptionalBiFunction<Optional<String>, PropertyMatcher, T, E> mapper) {
        return mapper.apply(
                Optional.ofNullable(
                        (String) get().remove(key)), this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T map(String key,
                                           String defaultValue,
                                           ExceptionalBiFunction<String, PropertyMatcher, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue : (String) value,
                this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapInt(String key, ExceptionalFunction<Optional<Integer>, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(
                        Integer::valueOf, (String) get().remove(key)
                ).getAsOptional())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapInt(String key,
                                              int defaultValue,
                                              ExceptionalFunction<Integer, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(
                                Integer::valueOf, (String) value).getOrThrowUnchecked())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapInt(String key, ExceptionalBiFunction<Optional<Integer>, PropertyMatcher, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(Integer::valueOf, (String) get().remove(key)).getAsOptional(),
                this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapInt(String key,
                                              int defaultValue,
                                              ExceptionalBiFunction<Integer, PropertyMatcher, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(
                                Integer::valueOf, (String) value).getOrThrowUnchecked(),
                this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapDouble(String key, ExceptionalFunction<Optional<Double>, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key)
                ).getAsOptional())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapDouble(String key,
                                                 double defaultValue,
                                                 ExceptionalFunction<Double, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(Double::valueOf, (String) value).getOrThrowUnchecked())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapDouble(String key, ExceptionalBiFunction<Optional<Double>, PropertyMatcher, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(
                        Double::valueOf, (String) get().remove(key)).getAsOptional(), this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapDouble(String key,
                                                 double defaultValue,
                                                 ExceptionalBiFunction<Double, PropertyMatcher, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(
                                Double::valueOf, (String) value).getOrThrowUnchecked(),
                this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapLong(String key, ExceptionalFunction<Optional<Long>, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key)
                ).getAsOptional())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapLong(String key,
                                               long defaultValue,
                                               ExceptionalFunction<Long, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(
                                Long::valueOf, (String) value).getOrThrowUnchecked())
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapLong(String key, ExceptionalBiFunction<Optional<Long>, PropertyMatcher, T, E> mapper) {
        return mapper.apply(
                ExceptionalFunction.supply(
                        Long::valueOf, (String) get().remove(key)).getAsOptional(), this)
                .getOrThrowUnchecked();
    }

    default <T, E extends Exception> T mapLong(String key,
                                               long defaultValue,
                                               ExceptionalBiFunction<Long, PropertyMatcher, T, E> mapper) {
        Object value = get().remove(key);
        return mapper.apply(
                value == null ? defaultValue :
                        ExceptionalFunction.supply(
                                Long::valueOf, (String) value).getOrThrowUnchecked(),
                this)
                .getOrThrowUnchecked();
    }
}
