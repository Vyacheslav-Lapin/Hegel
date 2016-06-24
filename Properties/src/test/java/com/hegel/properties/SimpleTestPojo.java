package com.hegel.properties;

import java.util.Objects;

public class SimpleTestPojo {
    final private String param2;
    final private int intParam;

    public SimpleTestPojo(String param2, int intParam) {
        this.param2 = param2;
        this.intParam = intParam;
    }

    @Override
    public boolean equals(Object o) {
        SimpleTestPojo that;
        return this == o
                || !(o == null || getClass() != o.getClass())
                && intParam == (that = (SimpleTestPojo) o).intParam
                && Objects.equals(param2, that.param2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(param2, intParam);
    }

    @Override
    public String toString() {
        return "SimpleTestPojo{param2='" + param2 +
                "\', intParam=" + intParam + '}';
    }
}
