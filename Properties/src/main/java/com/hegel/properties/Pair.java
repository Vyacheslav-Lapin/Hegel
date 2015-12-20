package com.hegel.properties;

import java.util.Map;

/**
 * @author Vyacheslav_Lapin
 */
public interface Pair<K, V> extends Map.Entry<K, V> {

    @Override
    default V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}
