package com.hegel.reflect;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassTest {

    @Test
    public void getClassTest() {
        assertEquals(getClass(), Class.wrap(this.getClass()).toSrc());
        assertEquals(TestClass.class, Class.wrap(TestClass.class).toSrc());
    }

    @Test
    public void getClassFieldTest() {
        TestClass testObj = new TestClass();
        ObjectField<String, TestClass> field = (ObjectField<String, TestClass>) Class.wrap(testObj).<String>getField("string").get();

        assertEquals(testObj.getString(), field.toString(testObj));
    }

    @Test
    public void getFieldsTest() {
        TestClass obj = new TestClass();
        Class<TestClass> aClass = Class.wrap(obj);

        // private String string;
        ObjectField<String, TestClass> stringField = (ObjectField<String, TestClass>) aClass.<String>getField("string").get();
        assertEquals(stringField.toSrc(), Field.wrap("string", aClass).get().toSrc());
        assertTrue(stringField.isPrivate());
        assertEquals(String.class, stringField.toSrc().getType());
        assertFalse(stringField.isFinal());
        assertFalse(stringField.isStatic());
        assertFalse(stringField.isTransient());
        assertFalse(stringField.isVolatile());
        assertNotNull(stringField.getValue(obj));

        // volatile int anInt = 5;
        IntField<TestClass> intField = (IntField<TestClass>) aClass.getField("anInt").get();
        assertTrue(intField.isVolatile());
        assertTrue(intField.isPackagePrivate());
        assertTrue(intField.isPrimitive());
        assertEquals(int.class, intField.toSrc().getType());
        assertNotEquals(Integer.class, intField.toSrc().getType());
        assertTrue(5 == intField.getValue(obj));

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
