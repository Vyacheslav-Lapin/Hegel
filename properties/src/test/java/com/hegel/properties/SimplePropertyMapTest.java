package com.hegel.properties;

import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Vyacheslav_Lapin
 */
public class SimplePropertyMapTest {

    static final String RESOURCES_DIR_PATH = "src/test/resources/";
    static final String CONFIG_FILE_PATH = RESOURCES_DIR_PATH + "test.properties";

    Properties properties = new Properties() {
        public Properties load(String filePath) {
            try (FileInputStream inStream = new FileInputStream(filePath)) {
                load(inStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
    }.load(CONFIG_FILE_PATH);

    @Test
    public void convertFromAndToLegacyClassProperties() throws Exception {
        PropertyMap propertyMap = PropertyMap.from(properties);
        assertEquals(propertyMap.toProperties(), properties);
        assertEquals(propertyMap.get("param2"), properties.getProperty("param2"));
    }

    @Test
    public void workWithLoad() {
        PropertyMap propertyMap = PropertyMap.fromFile(CONFIG_FILE_PATH);
        assertEquals(propertyMap.toProperties(), properties);
        assertEquals(propertyMap.get("param2"), properties.getProperty("param2"));
//        System.out.println(propertyMap);
    }

    @Ignore
    @Test
    public void getBean() {
        SimpleTestPojo simpleTestPojo = PropertyMap.get(CONFIG_FILE_PATH, SimpleTestPojo.class);

        PropertyMap props = PropertyMap.fromFile(CONFIG_FILE_PATH);
        SimpleTestPojo expected = new SimpleTestPojo(props.get("param2"), Integer.parseInt(props.get("intParam")));

        assertEquals(expected, simpleTestPojo);
    }
}
