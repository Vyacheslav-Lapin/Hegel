package com.hegel.orm;

import com.hegel.orm.columns.Column;
import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import org.omg.CORBA.Object;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;

public enum SqlType {
    BIT(boolean.class),
    TINYINT(byte.class),
    SMALLINT(short.class),
    INTEGER(int.class),
    BIGINT(long.class),
    REAL(float.class),
//    FLOAT(double.class),
    DOUBLE(double.class),
    BINARY(byte[].class),
//    VARBINARY(byte[].class),
//    LONGVARBINARY(byte[].class),
//    NCHAR(String.class),
//    NVARCHAR(String.class),
    LONGVARCHAR(String.class),
    NUMERIC(BigDecimal.class),
//    DECIMAL(BigDecimal.class),
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

    SqlType(java.lang.Class<?> type) {
        this(Class.wrap(type));
    }

    SqlType(Class<?> type) {
        this.type = type;
    }

    public static SqlType from(BaseType baseType) {
        return Arrays.stream(values()).filter(sqlType -> sqlType.baseType == baseType).findFirst().orElseGet(() ->);
    }

    public static SqlType from(Class<?> objectType) {
        return Arrays.stream(values()).filter(sqlType -> sqlType.type == objectType).findFirst().get();
    }

    public static <T, C> String toString(Column<T, C> cColumn) {
        return Arrays.stream(values())
                .filter(sqlType -> sqlType.baseType.getPrimitiveClass().equals(cColumn.getOwnerClass().toSrc()))
                .findAny().get().toString();
    }
}
