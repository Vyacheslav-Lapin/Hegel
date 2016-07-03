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
    OBJECT(Object.class);

    private Class<?> aClass;

    BaseType(java.lang.Class<?> aClass) {
        this.aClass = Class.wrap(aClass);
    }

    public Class<?> getType() {
        return aClass;
    }

    public static BaseType from(Class<?> type) {
        return Stream.of(BaseType.values())
                .filter(baseType -> type.isInherited(baseType.getType()))
                .findAny()
                .orElse(OBJECT);
    }
}
