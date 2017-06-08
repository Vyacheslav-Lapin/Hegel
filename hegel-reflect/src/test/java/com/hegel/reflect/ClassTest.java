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
        assertEquals(getClass(), Class.wrap(getClass()).get());
        assertEquals(TestClass.class, Class.wrap(TestClass.class).get());
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
        assertEquals(stringField.get(), Field.wrap("string", aClass).get().get());
        assertTrue(stringField.isPrivate());
        assertEquals(String.class, stringField.get().getType());
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
        assertEquals(int.class, intField.get().getType());
        assertNotEquals(Integer.class, intField.get().getType());
        assertTrue(5 == intField.getValue(testObj));

//        // public static double PI = Math.PI;
        DoubleField<TestClass> piXField = (DoubleField<TestClass>) aClass.getField("PI").get();
        assertFalse(piXField.isVolatile());
        assertTrue(piXField.isPublic());
        assertTrue(piXField.isPrimitive());
        assertTrue(piXField.isStatic());
        assertTrue(piXField.isFinal());
        assertEquals(double.class, piXField.get().getType());
        assertNotEquals(Double.class, piXField.get().getType());
        assertTrue(Math.PI == piXField.getValue());
    }
}
