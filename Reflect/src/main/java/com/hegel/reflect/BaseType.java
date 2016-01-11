package com.hegel.reflect;

import java.lang.Class;
import java.util.Optional;
import java.util.stream.Stream;

import static com.hegel.reflect.Class.isInherited;

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

    private java.lang.Class<?> aClass;

    BaseType(java.lang.Class<?> aClass) {
        this.aClass = aClass;
    }

    public Class<?> getAClass() {
        return aClass;
    }

    public static BaseType from(Class<?> type) {

        Optional<BaseType> baseTypeOptional = Stream.of(BaseType.values())
                .filter(baseType -> isInherited(type, baseType.getAClass()))
                .findAny();

        assert baseTypeOptional.isPresent();

        return baseTypeOptional.get();
    }
}
