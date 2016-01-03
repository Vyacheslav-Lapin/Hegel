package com.hegel.reflect;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassTest {

    @Test
    public void getClassTest() {
        assertEquals(this.getClass(), Class.wrap(this.getClass()).toSrc());
        assertEquals(TestClass.class, Class.wrap(TestClass.class).toSrc());
    }

    @Test
    public void getClassFieldTest() {
        TestClass testObj = new TestClass();
        Field<String, TestClass> field = Class.wrap(testObj).<String>getField("string").get();

        assertEquals(testObj.getString(), field.toString(testObj));
    }

    @Test
    public void getFieldsTest() {
        TestClass obj = new TestClass();
        Class<TestClass> aClass = Class.wrap(obj);

        // private String string;
        Field<String, TestClass> stringField = aClass.<String>getField("string").get();
        assertEquals(stringField.toSrc(), Field.wrap("string", aClass).get().toSrc());
        assertTrue(stringField.isPrivate());
        assertEquals(String.class, stringField.getType());
        assertFalse(stringField.isFinal());
        assertFalse(stringField.isStatic());
        assertFalse(stringField.isTransient());
        assertFalse(stringField.isVolatile());
        assertNotNull(stringField.getValue(obj));

        // volatile int anInt = 5;
//        Field<Integer, ClassTest> intField = aClass.getField("anInt", int.class).get();
//        assertTrue(intField.isVolatile());
//        assertTrue(intField.isPackagePrivate());
//        assertTrue(intField.isPrimitive());
//        assertEquals(int.class, intField.getType());
//        assertNotEquals(Integer.class, intField.getType());
//        assertTrue(5 == intField.getValue(obj));
//
//        // public static double PI = Math.PI;
//        StaticField<Double, ClassTest> piXField = aClass.getStaticField("PI", double.class);
//        assertFalse(piXField.isVolatile());
//        assertTrue(piXField.isPublic());
//        assertTrue(piXField.isPrimitive());
//        assertTrue(piXField.isStatic());
//        assertTrue(piXField.isFinal());
//        assertEquals(double.class, piXField.getType());
//        assertNotEquals(Double.class, piXField.getType());
//        assertTrue(Math.PI == piXField.getValue());
    }
}
