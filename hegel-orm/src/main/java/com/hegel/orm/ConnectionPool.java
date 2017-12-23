package com.hegel.orm;

import com.hegel.core.Pool;
import com.hegel.core.functions.ExceptionalBiFunction;
import com.hegel.core.functions.ExceptionalFunction;
import com.hegel.properties.PropertyMatcher;
import lombok.val;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ConnectionPool extends JdbcDao<ConnectionPool> {

    String DEFAULT_DB_PROPERTIES_FILE_NAME = "db.properties";
    String SQL_FILE_NAME_SUFFIX = ".sql";
    String JDBC_DRIVER_CLASS_KEY = "driver";
    String JDBC_URL_KEY = "url";
    String JDBC_CONNECTION_POOL_SIZE_KEY = "poolSize";
    String JDBC_INIT_SCRIPTS_FOLDER_KEY = "initScriptsPaths";

    static ConnectionPool create(String rootFilePath) {

        Supplier<InputStream> inputStreamSupplier = ExceptionalFunction.supplyUnchecked(FileInputStream::new,
                rootFilePath + DEFAULT_DB_PROPERTIES_FILE_NAME);

        //noinspection ConstantConditions
        return PropertyMatcher.from(inputStreamSupplier)
                .ensureOnlyThatKeysExists(
                        JDBC_DRIVER_CLASS_KEY,
                        JDBC_URL_KEY,
                        JDBC_CONNECTION_POOL_SIZE_KEY,
                        JDBC_INIT_SCRIPTS_FOLDER_KEY,
                        "user",
                        "password")
                .with(JDBC_DRIVER_CLASS_KEY, driverClassName -> Class.forName(driverClassName.get()))
                .map(JDBC_URL_KEY, (url, pm) ->
                        pm.mapInt(JDBC_CONNECTION_POOL_SIZE_KEY, 5, size ->
                                pm.map(JDBC_INIT_SCRIPTS_FOLDER_KEY, scriptsFolder ->
                                        ConnectionPool.create(size, url.get(), pm.get())
                                                .executeScripts(rootFilePath + scriptsFolder))));
    }

    static ConnectionPool create(int size, String jdbcUrl, Properties properties) {
        return new Pool<>(
                Connection.class,
                ExceptionalBiFunction.supplyUnchecked(
                        DriverManager::getConnection, jdbcUrl, properties),
                size
        )::get;
    }

    default ConnectionPool executeScripts(String dbFilesFolderPath) {

        if (dbFilesFolderPath == null)
            return this;

        if (!dbFilesFolderPath.endsWith("/"))
            dbFilesFolderPath += "/";

        List<Path> pathList = new ArrayList<>();
        Path path;
        for (int i = 0; (path = Paths.get(dbFilesFolderPath + ++i + SQL_FILE_NAME_SUFFIX)).toFile().exists(); )
            pathList.add(path);

        return executeScripts(pathList.toArray(new Path[0]));
    }

    default <T> CompletableFuture<T> mapConnectionAsync(Function<Connection, T> connectionExtractor) {
        return CompletableFuture.supplyAsync(() -> {
            try (val connection = get()) {
                return connectionExtractor.apply(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
