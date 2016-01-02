package com.hegel.reflect;

import java.lang.reflect.Method;

public class XMethod<R, C> {

//    private Method method;
//
//    private XMethod(Method method) {
//        this.method = method;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <R, C> XMethod<R, C> wrap(Method method) {
//        return wrap(method, (Class<R>) method.getReturnType());
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <R, C> XMethod<R, C> wrap(Method method, Class<R> returnType) {
//        assert method.getReturnType().equals(returnType);
//        return wrap(method, returnType, (Class<C>) method.getDeclaringClass());
//    }
//
//    public static <R, C> XMethod<R, C> wrap(Method method, Class<R> returnType, Class<C> declaringClass) {
//        assert method.getReturnType().equals(returnType);
//        assert method.getDeclaringClass().equals(declaringClass);
//
//        return new XMethod<>(method);
//    }
//
//    public Method toSrc() {
//        return method;
//    }
//
//    @SuppressWarnings("unchecked")
//    public R getReturnType() {
//        return (R) method.getReturnType();
//    }
//
//    @SuppressWarnings("unchecked")
//    public C getDeclaringClass() {
//        return (C) method.getDeclaringClass();
//    }
}
