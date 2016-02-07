package com.hegel.reflect;

import com.hegel.reflect.fields.DoubleField;
import com.hegel.reflect.fields.Field;
import com.hegel.reflect.fields.IntField;
import com.hegel.reflect.fields.ObjectField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassTest {

    TestClass testObj;

    @Before
    public void init() {
        testObj = new TestClass();
    }

    @Test
    public void toSrcTest() {
        assertEquals(getClass(), Class.wrap(getClass()).toSrc());
        assertEquals(TestClass.class, Class.wrap(TestClass.class).toSrc());
    }

    @Test
    public void getClassFieldTest() {
        ObjectField<String, TestClass> field = (ObjectField<String, TestClass>) Class.wrap(testObj).getField("string").get();

        assertEquals(testObj.getString(), field.toString(testObj));
    }

    @Test
    public void getFieldsTest() {
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

    @Test
    public void calculateDbFields() {
        Class<TestClass> aClass = Class.wrap(TestClass.class);
        aClass.dynamicFields()
                .map(Field::toSqlName)
                .peek(System.out::println)
                .map(Field::fromSqlName)
                .forEach(System.out::println);
    }

    @Test
    public void calculateDbQuery() {
        assertEquals("select id, name, login, password, is_txt_enable from User", Class.wrap(User.class).sqlSelectQuery());
    }
}
