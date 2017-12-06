package com.hegel.reflect;

public interface Reflect {

    static void loadClass(String className) {
        loadClass(className, "Can't find class " + className);
    }

    static void loadClass(String className, String message) {
        try {
            java.lang.Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(message, e);
        }
    }
}
