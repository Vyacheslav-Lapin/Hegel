package com.hegel.reflect;

import java.util.stream.Stream;

public enum BaseType {
    BOOLEAN(boolean.class),
    BYTE(byte.class),
    CHAR(char.class),
    SHORT(short.class),
    INT(int.class),
    LONG(long.class),
    FLOAT(float.class),
    DOUBLE(double.class),
    REFERENCE(Object.class);

    private Class<?> aClass;

    BaseType(java.lang.Class<?> aClass) {
        this(Class.wrap(aClass));
    }

    BaseType(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Class<?> getType() {
        return aClass;
    }

    public static BaseType from(Class<?> type) {
        return Stream.of(BaseType.values())
                .filter(baseType -> type.isInherited(baseType.getType()))
                .findAny()
                .orElse(REFERENCE);
    }

    public static BaseType from(java.lang.Class<?> type) {
        return from(Class.wrap(type));
    }
}
