package com.hegel.reflect;

import java.lang.reflect.Modifier;

public class StaticField<F, C> //extends Field<F, C>
{

//    public StaticField(java.lang.reflect.Field field) {
//        super(field);
//    }
//
//    public StaticField(java.lang.reflect.Field field, Class<F> type) {
//        super(field, type);
//    }
//
//    public StaticField(java.lang.reflect.Field field, Class<F> type, Class<C> declaringClass) {
//        super(field, type, declaringClass);
//    }
//
//    public static <F, C> StaticField<F, C> wrap(java.lang.reflect.Field field, Class<F> type, Class<C> declaringClass) {
//        assert Modifier.isStatic(field.getModifiers());
//        assert field.getType().equals(type);
//        assert field.getDeclaringClass().equals(declaringClass);
//
//        return new StaticField<>(field, type, declaringClass);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <F, C> StaticField<F, C> wrap(java.lang.reflect.Field field) {
//        assert Modifier.isStatic(field.getModifiers());
//        return wrap(field, (Class<F>) field.getType(), (Class<C>) field.getDeclaringClass());
//    }
//
//    public F getValue() {
//        return getValue(null);
//    }
}
