package com.hegel.reflect;

import java.lang.Class;
import java.util.Arrays;

import static com.hegel.reflect.Class.isInherited;

public enum BaseType {
    INT(int.class, Integer.class),
    DOUBLE(double.class, Double.class),
    BOOLEAN(boolean.class, Boolean.class),
    LONG(long.class, Long.class),
    CHAR(char.class, Character.class),
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    FLOAT(float.class, Float.class),
    REFERENCE(null, Object.class);

    public final java.lang.Class<?> primitiveClass;
    public final java.lang.Class<?> objectClass;

    BaseType(java.lang.Class<?> primitiveClass, java.lang.Class<?> objectClass) {
        this.primitiveClass = primitiveClass;
        this.objectClass = objectClass;
    }

    public static BaseType from(com.hegel.reflect.Class<?> type) {
        return from(type.toSrc());
    }

    public static BaseType from(Class<?> type) {
        return Arrays.stream(values())
                .filter(baseType -> type == baseType.primitiveClass || isInherited(type, baseType.objectClass))
                .findAny()
                .orElseThrow(() -> new AssertionError("Could not find relevant type for " + type + " class!"));
    }

    public static Class<?> boxType(com.hegel.reflect.Class<?> type) {
        return boxType(type.toSrc());
    }

    public static Class<?> boxType(Class<?> type) {
        return Arrays.stream(values())
                .filter(baseType -> type == baseType.primitiveClass)
                .findAny()
                .<Class<?>>map(baseType -> baseType.objectClass)
                .orElse(type);
    }
}
