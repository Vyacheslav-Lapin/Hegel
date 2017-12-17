package com.hegel.orm;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;

public enum JdbcType {
    BIT(BaseType.BOOLEAN),
    TINYINT(BaseType.BYTE),
    SMALLINT(BaseType.SHORT),
    INTEGER(BaseType.INT),
    BIGINT(BaseType.LONG),
    REAL(BaseType.FLOAT),
    DOUBLE(BaseType.DOUBLE),
    BINARY(byte[].class),
    LONGVARCHAR(String.class),
    NUMERIC(BigDecimal.class),
    DATE(Date.class),
    TIME(Time.class),
    TIMESTAMP(Timestamp.class),
    CLOB(Clob.class),
    BLOB(Blob.class),
    ARRAY(Array.class),
    DISTINCT(Object.class),
    STRUCT(Struct.class),
    REF(Ref.class);

    private Class<?> type;

    JdbcType(BaseType baseType) {
        this(baseType.getType());
    }

    JdbcType(java.lang.Class<?> type) {
        this(Class.wrap(type));
    }

    JdbcType(Class<?> type) {
        this.type = type;
    }

    public static JdbcType from(BaseType baseType) {
        return from(baseType.getType());
    }

    public static JdbcType from(Class<?> objectType) {
        return Arrays.stream(values())
                .filter(jdbcType -> jdbcType.type.equals(objectType))
                .findFirst()
                .orElse(DISTINCT);
    }
}
