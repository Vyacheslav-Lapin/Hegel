package com.hegel.reflect;

import org.junit.Test;

import static org.junit.Assert.*;

public class XClassTest {

    private String string;
    volatile int anInt = 5;
    public static final double PI = Math.PI;

    @Test
    public void getClassTest() {
        assertEquals(this.getClass(), XClass.from(this.getClass()).toClass());
    }

    @Test
    public void getFieldsTest() {
        XClass<XClassTest> xClass = XClass.from(XClassTest.class);
//        XClass<? extends XClassTest> xClass = XClass.from(this.getClass());

        XClassTest object = new XClassTest();

        // private String string;
        XField<String, XClassTest> stringXField = xClass.getField("string", String.class);
        assertTrue(stringXField.isPrivate());
        assertEquals(String.class, stringXField.getType());
        assertFalse(stringXField.isFinal());
        assertFalse(stringXField.isStatic());
        assertFalse(stringXField.isTransient());
        assertFalse(stringXField.isVolatile());
        assertNull(stringXField.getValue(object));

        // volatile int anInt = 5;
        XField<Integer, XClassTest> intXField = xClass.getField("anInt", int.class);
        assertTrue(intXField.isVolatile());
        assertTrue(intXField.isPackagePrivate());
        assertTrue(intXField.isPrimitive());
        assertEquals(int.class, intXField.getType());
        assertNotEquals(Integer.class, intXField.getType());
        assertTrue(5 == intXField.getValue(object));

        // public static double PI = Math.PI;
        StaticXField<Double, XClassTest> piXField = xClass.getStaticField("PI", double.class);
        assertFalse(piXField.isVolatile());
        assertTrue(piXField.isPublic());
        assertTrue(piXField.isPrimitive());
        assertTrue(piXField.isStatic());
        assertTrue(piXField.isFinal());
        assertEquals(double.class, piXField.getType());
        assertNotEquals(Double.class, piXField.getType());
        assertTrue(Math.PI == piXField.getValue());
    }
}
