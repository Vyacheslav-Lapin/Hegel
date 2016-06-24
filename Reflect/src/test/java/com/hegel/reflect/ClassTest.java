package com.hegel.reflect;

import com.hegel.core.test.MappedMatcher;
import com.hegel.core.wrappers.Wrapper;
import com.hegel.reflect.fields.DoubleField;
import com.hegel.reflect.fields.Field;
import com.hegel.reflect.fields.IntField;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static com.hegel.core.test.Test.get;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ClassTest {

    private TestClass testObj;

    @Before
    public void init() {
        testObj = new TestClass();
    }

    @Test
    public void toSrcTest() {
        assertEquals(getClass(), Class.wrap(getClass()).toSrc());
        assertThat(Class.wrap(TestClass.class), isWrapperOf(TestClass.class));
    }

    @Test
    public void getClassFieldTest() {
        final String string = Class.wrap(testObj).getField("string")
                .map(field -> field.toString(testObj))
                .orElseThrow(AssertionError::new);
        assertThat(string, is(testObj.getString()));
    }

    @Test
    public void getStringFieldsTest() {
        Class<TestClass> aClass = Class.wrap(testObj);

        // private String string;
        Field<String, TestClass> field = get(aClass.getField("string"),
                "There is no such field as 'stringField' in that class!");

        assertThat("That class must have field 'string'", Field.wrap("string", aClass), isWrapperOf(field));

        // type checks
        assertThat(field.toSrc().getType(), is((Object) String.class));
        assertThat(field.getType(), isWrapperOf(String.class));

        // modifier checks
        assertTrue(field.isPrivate());
        assertFalse(field.isFinal());
        assertFalse(field.isStatic());
        assertFalse(field.isTransient());
        assertFalse(field.isVolatile());

        // value checks
        assertNotNull(field.value(testObj));
    }

    @Test
    public void getIntFieldsTest() {
        Class<TestClass> aClass = Class.wrap(testObj);

        // volatile int anInt = 5;
        IntField<TestClass> intField = get(aClass.getField("anInt"));

        // type checks
        assertEquals(int.class, intField.toSrc().getType());
        assertEquals(int.class, intField.getType().toSrc());
        assertTrue(intField.isPrimitive());

        // modifier checks
        assertTrue(intField.isVolatile());
        assertTrue(intField.isPackagePrivate());

        // value checks
        assertTrue(5 == intField.primitiveValue(testObj));
    }

    @Test
    public void getDoubleFieldsTest() {
        Class<TestClass> aClass = Class.wrap(testObj);

        // public static double PI = Math.PI;
        DoubleField<TestClass> doubleField = get(aClass.getField("PI"));

        // type checks
        assertEquals(double.class, doubleField.toSrc().getType());
        assertEquals(double.class, doubleField.getType().toSrc());
        assertTrue(doubleField.isPrimitive());

        // modifier checks
        assertFalse(doubleField.isVolatile());
        assertTrue(doubleField.isPublic());
        assertTrue(doubleField.isStatic());
        assertTrue(doubleField.isFinal());

        // value checks
        assertTrue(Math.PI == doubleField.primitiveValue());
    }
}
