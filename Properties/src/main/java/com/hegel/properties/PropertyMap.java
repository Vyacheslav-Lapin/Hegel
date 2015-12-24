package com.hegel.properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface PropertyMap extends Map<String, String> {

    static PropertyMap from(Properties properties) {
        return properties.stringPropertyNames().stream().parallel()
                .reduce(new SimplePropertyMap(),
                        (props, s) -> {
                            props.put(s, properties.getProperty(s));
                            return props;
                        },
                        (props1, props2) -> {
                            props1.putAll(props2);
                            return props1;
                        });
    }

    static PropertyMap from(Map<String, String> stringMap) {
        return new SimplePropertyMap(stringMap);
    }

    static PropertyMap from(Path path) {
        try {
            return PropertyMap.from(
                    Files.lines(path).parallel()
                            .map(s -> s.split("="))
                            .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static PropertyMap fromFile(String filePath) {
        return from(Paths.get(filePath));
    }

    default Properties toProperties() {
        Properties properties = new Properties();
        properties.putAll(this);
        return properties;
    }

//    static <T> T get(String configFilePath, Class<T> tClass) {
//
//        PropertyMap props = fromFile(configFilePath);
//        XClass<T> xClass = XClass.from(tClass);
//
//        try {
//            T t = xClass.getConstructors().;
//
//            Stream.of(tClass.getFields())
//                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
//                    .forEach(field -> parseSet(props.get(field.getName()), t, field));
//
//            return t;
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
