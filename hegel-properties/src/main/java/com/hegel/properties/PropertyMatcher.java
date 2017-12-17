package com.hegel.properties;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public interface PropertyMatcher extends Supplier<Properties> {

    @SneakyThrows
    static PropertyMatcher from(String fileName) {
        if (!fileName.endsWith(".properties"))
            fileName += ".properties";

//        try (InputStream fileInputStream = PropertyMatcher.class.getResourceAsStream(fileName)) {
        try (InputStream fileInputStream = new FileInputStream(fileName)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return () -> properties;
        }
    }
}
