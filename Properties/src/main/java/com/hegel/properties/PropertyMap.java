package com.hegel.properties;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import com.hegel.reflect.Constructor;
import com.hegel.reflect.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public interface PropertyMap extends Map<String, String> {

    static PropertyMap from(Properties properties) {
        return properties.stringPropertyNames().stream().parallel()
                .reduce(new SimplePropertyMap(),
                        (props, s) -> {
                            props.put(s, properties.getProperty(s));
                            return props;
                        },
                        (props, propsToAdd) -> {
                            props.putAll(propsToAdd);
                            return props;
                        });
    }

    static PropertyMap from(Map<String, String> stringMap) {
        return new SimplePropertyMap(stringMap);
    }

    static PropertyMap from(Path path) {
        try {
            Stream<String[]> stringPairs = Files.lines(path).map(s -> s.split("=", 2));
            return from(stringPairs.collect(toMap(stringPair -> stringPair[0], stringPair -> stringPair[1])));
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

    @SuppressWarnings("unchecked")
    static <T> T get(String configFilePath, java.lang.Class<T> aClass) {

        PropertyMap propertyMap = fromFile(configFilePath);

        Constructor<T> constructor = Class.wrap(aClass)
                .findConstructorByParamNames(propertyMap.keySet())
                .orElseThrow(() -> new AssertionError("Can`t find relevant constructor!"));

        return constructor.execute(constructor.parameters()
                .map(parameter -> toObject(propertyMap, parameter))
                .toArray());
    }

    static <T, C> T toObject(PropertyMap propertyMap, Parameter<T, C, java.lang.reflect.Constructor<C>> parameter) {
        return toObject(propertyMap.get(parameter.getName()), parameter.getType().toSrc()); // TODO: 3/27/2016 think about more complicated cases
    }

    @SuppressWarnings("unchecked")
    static <T> T toObject(String property, java.lang.Class<T> aClass) {
        switch (BaseType.from(aClass)) {
            case INT:
                return (T) Integer.decode(property);
            case DOUBLE:
                return (T) Double.valueOf(property);
            case BOOLEAN:
                return (T) Boolean.valueOf(property);
            case LONG:
                return (T) Long.decode(property);
            case CHAR:
                return (T) Character.valueOf(property.charAt(0));
            case FLOAT:
                return (T) Float.valueOf(property);
            case BYTE:
                return (T) Byte.decode(property);
            case SHORT:
                return (T) Short.decode(property);
            default:
                assert aClass == String.class;
                return (T) property;
        }
    }
}
