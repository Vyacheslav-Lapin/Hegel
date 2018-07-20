package com.hegel.core.streams;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtensionMethod(StreamUtils.class)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class StreamUtilsTest {

    @Test
    @DisplayName("\"ToProperties\" method works correctly")
    void testToProperties() {

        // given
        int i = 10;

        @Value
        class Entry<K, V> implements Map.Entry<K, V> {
            K key;
            V value;

            @Override
            public V setValue(V value) {
                throw new UnsupportedOperationException();
            }
        }

        Stream<Entry<String, String>> entryStream = IntStream.range(0, i)
                .mapToObj(String::valueOf)
                .map(s -> new Entry<>(s, s));

        // when
        Properties properties = entryStream.collect(StreamUtils.toProperties());

        // then
        assertEquals(properties.size(), i);
        assertThat(
                IntStream.range(0, i)
                        .mapToObj(String::valueOf)
                        .filter(properties::containsKey)
                        .filter(s -> properties.getProperty(s).equals(s))
                        .count())
                .isEqualTo(10L);
    }

    @Test
    @SneakyThrows
    @DisplayName("\"ToStream\" method works correctly")
    void testToStream() {
        // given
        Iterator<Integer> integerIterator = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

        // when
        Stream<Integer> integerStream = StreamUtils.toStream(integerIterator);

        // then
        assertThat(integerStream).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

}