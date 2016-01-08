/*
 * Canadian Tire Corporation, Ltd. Do not reproduce without permission in writing.
 * Copyright (c) 2016 Canadian Tire Corporation, Ltd. All rights reserved.
 */

package com.hegel.reflect;

/**
 * @author TestClass
 */
public class TestClass {
    private String string = "мама мыла раму";
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
