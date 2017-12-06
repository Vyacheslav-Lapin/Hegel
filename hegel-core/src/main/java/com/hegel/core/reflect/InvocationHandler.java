package com.hegel.core.reflect;

import com.hegel.core.functions.ExceptionalFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;

@FunctionalInterface
public interface InvocationHandler<T> extends java.lang.reflect.InvocationHandler {

    static <T> Function<InvocationHandler<T>, T> getProxyMakerFor(Class<T> anInterface) {
        //noinspection unchecked
        return invocationHandler -> (T) Proxy.newProxyInstance(
                anInterface.getClassLoader(), new Class[]{anInterface}, invocationHandler);
    }

    Object apply(T proxy, Method method, Function<T, ?> chain, Object[] args);

    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class)
            switch (method.getName()) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return String.format(
                            "%s@%x, with InvocationHandler %s",
                            proxy.getClass().getName(),
                            System.identityHashCode(proxy),
                            this);
                default:
                    throw new IllegalStateException(String.valueOf(method));
            }
        //noinspection unchecked
        return apply((T) proxy, method, ExceptionalFunction.toUncheckedFunction(t -> method.invoke(t, args)), args);
    }
}
