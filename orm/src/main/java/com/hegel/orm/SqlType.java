package com.hegel.orm;

import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import org.omg.CORBA.Object;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;

import static com.hegel.reflect.BaseType.*;

public enum SqlType {
    BIT(BOOLEAN),
    TINYINT(BYTE),
    SMALLINT(SHORT),
    INTEGER(INT),
    BIGINT(LONG),
    REAL(BaseType.FLOAT),
    FLOAT(BaseType.DOUBLE),
    DOUBLE(BaseType.DOUBLE),
    BINARY(byte[].class),
    VARBINARY(byte[].class),
    LONGVARBINARY(byte[].class),
    NCHAR(String.class),
    NVARCHAR(String.class),
    LONGVARCHAR(String.class),
    NUMERIC(BigDecimal.class),
    DECIMAL(BigDecimal.class),
    DATE(Date.class),
    TIME(Time.class),
    TIMESTAMP(Timestamp.class),
    CLOB(Clob.class),
    BLOB(Blob.class),
    ARRAY(Array.class),
    DISTINCT(Object.class),
    STRUCT(Struct.class),
    REF(Ref.class);

    private BaseType baseType;
    private Class<?> objectType;

    SqlType(BaseType baseType) {
        this.baseType = baseType;
    }

    SqlType(java.lang.Class<?> objectType) {
        this(Class.wrap(objectType));
    }

    SqlType(Class<?> objectType) {
        this(OBJECT);
        this.objectType = objectType;
    }

    public static <C> String toString(Column<C> cColumn) {
        return Arrays.stream(values())
                .filter(sqlType -> sqlType.baseType.getType().equals(cColumn.toSrc()))
                .findAny();
    }
}
