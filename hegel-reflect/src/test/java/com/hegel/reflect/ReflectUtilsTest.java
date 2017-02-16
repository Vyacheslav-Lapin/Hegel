package com.hegel.reflect;

import com.hegel.core.reflect.InvocationHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReflectUtilsTest {

    @FunctionalInterface
    public interface TestFuncInt {
        String hello(String name);
    }

    @Test
    void baseProxyUsage() throws Exception {
        TestFuncInt realObj = name -> String.format("Hello, %s!", name);
        TestFuncInt proxyObj = (TestFuncInt) Proxy.newProxyInstance(
                TestFuncInt.class.getClassLoader(),
                new java.lang.Class<?>[]{TestFuncInt.class},
                (proxy, method, args) -> {
                    if (method.getDeclaringClass() == Object.class)
                        switch (method.getName()) {
                            case "equals":
                                return args[0] != null && args[0].equals(realObj);
                            case "hashCode":
                                return System.identityHashCode(realObj);
                            case "toString":
                                return proxy.getClass().getName() + "@" +
                                        Integer.toHexString(System.identityHashCode(proxy)) +
                                        ", with InvocationHandler";
                            default:
                                throw new IllegalStateException(String.valueOf(method));
                        }
                    return realObj.hello((String) args[0]) + " from Proxy!";
                });

        assertThat(proxyObj.hello("Duke"), is(realObj.hello("Duke") + " from Proxy!"));
        assertThat(proxyObj.toString(), not(realObj.toString()));
        assertThat(proxyObj.hashCode(), is(System.identityHashCode(realObj)));
        assertEquals(proxyObj, proxyObj);
        assertNotEquals(proxyObj, new Object());
        assertNotEquals(proxyObj, null);
    }

    @Test
    void getProxyMakerFor() throws Exception {
        TestFuncInt realObj = name -> String.format("Hello %s!", name);
        TestFuncInt proxyObj = InvocationHandler.getProxyMakerFor(TestFuncInt.class).apply(
                (proxy, method, chain, args) ->
                        method.getName().equals("hello") ?
                                chain.apply(realObj) + " from Proxy!" :
                                chain.apply(realObj));

        assertThat(proxyObj.hello("Duke"), is(realObj.hello("Duke") + " from Proxy!"));
        assertThat(proxyObj.toString(), not(realObj.toString()));
        assertThat(proxyObj.hashCode(), is(System.identityHashCode(proxyObj)));
        assertEquals(proxyObj, proxyObj);
        assertNotEquals(proxyObj, new Object());
        assertNotEquals(proxyObj, null);
    }
}