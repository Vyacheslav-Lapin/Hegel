package com.hegel.properties;

import java.util.Properties;

public interface PropertyUtils {
    static String getAndRemove(Properties properties, String key) {
        return (String) properties.remove(key);
    }
}
