package com.hegel.properties;

import java.util.HashMap;
import java.util.Map;

public class SimplePropertyMap extends HashMap<String, String> implements PropertyMap {

    public SimplePropertyMap() {
    }

    public SimplePropertyMap(Map<String, String> stringMap) {
        putAll(stringMap);
    }
}
