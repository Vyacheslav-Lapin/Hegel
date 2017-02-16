package com.hegel.reflect;

import com.hegel.reflect.fields.DoubleField;
import com.hegel.reflect.fields.Field;
import com.hegel.reflect.fields.IntField;
import com.hegel.reflect.fields.ObjectField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest {

    TestClass testObj;

    @BeforeEach
    void init() {
        testObj = new TestClass();
    }

    @Test
    void toSrcTest() {
        assertEquals(getClass(), Class.wrap(getClass()).toSrc());
        assertEquals(TestClass.class, Class.wrap(TestClass.class).toSrc());
    }

    @Test
    void getClassFieldTest() {
        ObjectField<String, TestClass> field = (ObjectField<String, TestClass>) Class.wrap(testObj).getField("string").get();

        assertEquals(testObj.getString(), field.toString(testObj));
    }

    @Test
    void getFieldsTest() {
        Class<TestClass> aClass = Class.wrap(testObj);

        // private String string;
        ObjectField<String, TestClass> stringField = (ObjectField<String, TestClass>) aClass.getField("string").get();
        assertEquals(stringField.toSrc(), Field.wrap("string", aClass).get().toSrc());
        assertTrue(stringField.isPrivate());
        assertEquals(String.class, stringField.toSrc().getType());
        assertFalse(stringField.isFinal());
        assertFalse(stringField.isStatic());
        assertFalse(stringField.isTransient());
        assertFalse(stringField.isVolatile());
        assertNotNull(stringField.getValue(testObj));

        // volatile int anInt = 5;
        IntField<TestClass> intField = (IntField<TestClass>) aClass.getField("anInt").get();
        assertTrue(intField.isVolatile());
        assertTrue(intField.isPackagePrivate());
        assertTrue(intField.isPrimitive());
        assertEquals(int.class, intField.toSrc().getType());
        assertNotEquals(Integer.class, intField.toSrc().getType());
        assertTrue(5 == intField.getValue(testObj));

//        // public static double PI = Math.PI;
        DoubleField<TestClass> piXField = (DoubleField<TestClass>) aClass.getField("PI").get();
        assertFalse(piXField.isVolatile());
        assertTrue(piXField.isPublic());
        assertTrue(piXField.isPrimitive());
        assertTrue(piXField.isStatic());
        assertTrue(piXField.isFinal());
        assertEquals(double.class, piXField.toSrc().getType());
        assertNotEquals(Double.class, piXField.toSrc().getType());
        assertTrue(Math.PI == piXField.getValue());
    }
}
