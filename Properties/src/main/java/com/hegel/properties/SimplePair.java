package com.hegel.properties;

public class SimplePair<K, V> implements Pair<K, V> {

    public final K key;
    public final V value;

    public SimplePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
