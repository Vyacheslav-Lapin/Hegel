package com.hegel.reflect;

public class TestClass {

    private String string = "some text";

    volatile int anInt = 5;

    public static final double PI = Math.PI;

    public static Integer counter;

    public String getString() {
        return string;
    }

    public int getAnInt() {
        return anInt;
    }

    public static double getPI() {
        return PI;
    }
}
