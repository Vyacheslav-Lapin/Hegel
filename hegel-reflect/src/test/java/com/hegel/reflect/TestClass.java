package com.hegel.reflect;

public class TestClass {

    public static final double PI = Math.PI;
    public static Integer counter;
    volatile int anInt = 5;
    private String string = "мама мыла раму";

    public static double getPI() {
        return PI;
    }

    public String getString() {
        return string;
    }

    public int getAnInt() {
        return anInt;
    }
}
