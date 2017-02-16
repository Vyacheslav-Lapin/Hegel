package com.hegel.orm.columns;

import com.hegel.reflect.fields.Field;

@FunctionalInterface
public interface LongColumn<C> extends Column<C> {

    static <C> Column<C> wrap(Field<C> cField) {
        return cField::toSrc;
    }
}
