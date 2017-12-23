package com.hegel.properties;

import com.hegel.core.functions.ExceptionalFunction;
import com.hegel.core.functions.ExceptionalSupplier;
import com.hegel.core.test.Tests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class PropertyMatcherTest {

    public static final String SRC_TEST_RESOURCES = "./src/test/resources/";

    PropertyMatcher matcher = PropertyMatcher.from(
            ExceptionalFunction.supplyUnchecked(FileInputStream::new, SRC_TEST_RESOURCES +
//            () -> PropertyMatcherTest.class.getResourceAsStream(
                    "test.properties"));

    @Test
    @DisplayName("create method works correctly")
    void create() {
        Properties properties = matcher.get();
        assertThat(properties.getProperty("param2"), is("value1"));
        assertThat(properties.getProperty("intParam"), is("123"));
    }

    @Test
    @DisplayName("TypicalUsage method works correctly")
    @SneakyThrows
    void typicalUsage() {

        assertThat(
                Tests.fromSystemOutPrintln(() -> matcher.with("param2", System.out::println)),
                is("Optional[value1]"));

        assertThat(matcher.get().size(), is(1));

        ExceptionalSupplier<Connection, SQLException> getConnection = PropertyMatcher.from(
                ExceptionalFunction.supplyUnchecked(FileInputStream::new, SRC_TEST_RESOURCES + "db.properties"))
                .with("driver", driver -> Class.forName(driver.get()))
                .map("url", (url, pm) ->
//                        pm.mapInt("poolSize", poolSize ->
                                () -> DriverManager.getConnection(url.get(), pm.get()));

        try (Connection connection = getConnection.executeOrThrowUnchecked()) {
            assertThat(connection, notNullValue());
        }
//                .partialTransform("poolSize", Integer::parseInt, SimplePool::new, rp ->
//                        rp.partialTransform("url", DriverManager::getConnection, rp ->
//                                rp.partialRest("user", "password")))
        ;
    }

    // TODO: 20/12/2017 make method for extract key-value pairs subset
}