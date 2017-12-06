package com.hegel.orm;

import com.hegel.core.Pool;
import com.hegel.core.functions.ExceptionalBiFunction;
import com.hegel.properties.PropertyMap;
import com.hegel.reflect.Reflect;
import lombok.val;

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

@FunctionalInterface
public interface ConnectionPool extends JdbcDao<ConnectionPool> {

    String DEFAULT_DB_PROPERTIES_FILE_NAME = "db.properties";
    String SQL_FILE_NAME_SUFFIX = ".sql";
    String JDBC_DRIVER_CLASS_KEY = "driver";
    String JDBC_URL_KEY = "url";
    String JDBC_CONNECTION_POOL_SIZE_KEY = "poolSize";
    String JDBC_INIT_SCRIPTS_FOLDER_KEY = "initScriptsPaths";

    static ConnectionPool create() {
        return create("");
    }

    static ConnectionPool create(final String rootFilePath) {
        val props = PropertyMap.fromFile(rootFilePath + DEFAULT_DB_PROPERTIES_FILE_NAME);
        Reflect.loadClass(props.remove(JDBC_DRIVER_CLASS_KEY));
        return ConnectionPool.create(
                Integer.parseInt(props.remove(JDBC_CONNECTION_POOL_SIZE_KEY)),
                props.remove(JDBC_URL_KEY),
                props.toProperties())
                .executeScripts(rootFilePath + props.remove(JDBC_INIT_SCRIPTS_FOLDER_KEY));
    }

//    @SuppressWarnings("unused")
//    @SneakyThrows
//    static ConnectionPool create(String dbFilesFolderPath) {
//        try (InputStream inputStream = Files.newInputStream(Paths.map(dbFilesFolderPath + DEFAULT_DB_PROPERTIES_FILE_NAME))) {
//            Properties properties = new Properties();
//            properties.load(inputStream);
//            return create(properties, dbFilesFolderPath);
//        }
//    }

//    static ConnectionPool create(Properties properties, String dbFilesFolderPath) {
//        ExceptionalConsumer.put(Class::forName, (String) properties.remove(JDBC_DRIVER_CLASS_KEY));
//        String jdbcUrl = (String) properties.remove(JDBC_URL_KEY);
//        int size = Integer.parseInt((String) properties.remove(JDBC_CONNECTION_POOL_SIZE_KEY));
//        return create(jdbcUrl, properties, size, dbFilesFolderPath);
//    }

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
