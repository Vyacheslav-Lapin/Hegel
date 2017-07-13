package com.hegel.properties;

import com.hegel.reflect.Executable;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public interface PropertyMap extends Map<String, String> {

    static PropertyMap from(Properties properties) {
        return properties.stringPropertyNames().stream()
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

    @SneakyThrows
    static PropertyMap from(Path path) {
        return PropertyMap.from(
                Files.lines(path).parallel()
                        .map((String s) -> s.split("=", 2))
                        .collect(Collectors.toMap(strings -> strings[0], strings -> strings.length > 1 ? strings[1] : ""))
        );
    }

    static PropertyMap fromFile(String filePath) {
        return from(Paths.get(filePath));
    }

    default Properties toProperties() {
        Properties properties = new Properties();
        properties.putAll(this);
        return properties;
    }

    static PropertyMap from(String... params) {

        assert params.length % 2 == 0;

        PropertyMap result = new SimplePropertyMap();
        for (int index = 0; index < params.length; )
            result.put(params[index++], params[index++]);

        return result;
    }

    static <T> T get(String configFilePath, Executable<T, ?, ?> executable) {

//        executable.toMethodHandler

        return null;
    }

//    static <T> T get(String configFilePath, Class<T> tClass) {
//
//        PropertyMap props = fromFile(configFilePath);
//        Class<T> xClass = Class.wrap(tClass);
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
