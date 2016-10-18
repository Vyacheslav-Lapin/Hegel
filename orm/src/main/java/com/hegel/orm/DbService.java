package com.hegel.orm;

import com.hegel.core.Private;
import com.hegel.core.functions.ExceptionalConsumer;
import com.hegel.core.functions.ExceptionalFunction;
import com.hegel.core.functions.ExceptionalRunnable;
import com.hegel.core.functions.ExceptionalSupplier;
import com.hegel.properties.PropertyMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@FunctionalInterface
public interface DbService extends Supplier<Connection> {

    @Private
    String USER_KEY = "user";

    @Private
    String PASSWORD_KEY = "password";

    @Private
    String DRIVER_KEY = "driver";

    @Private
    String URL_KEY = "url";

    static DbService from(String driver, String jdbcUrl, String user, String password) {
        return from(driver, jdbcUrl, PropertyMap.from(USER_KEY, user, PASSWORD_KEY, password).toProperties());
    }

    static DbService from(String driver, String jdbcUrl, Properties properties) {
        assert properties.containsKey(USER_KEY);
        assert properties.containsKey(PASSWORD_KEY);
        ExceptionalRunnable.run(() -> Class.forName(driver));
        return from(ExceptionalSupplier.toUncheckedSupplier(() -> DriverManager.getConnection(jdbcUrl, properties)));
    }

    static DbService from(Supplier<Connection> connectionSupplier) {
        return connectionSupplier::get;
    }

    static DbService from(String dbPropertiesFilePath, String... dbInitScriptFilePaths) {

        DbService dbService = from(PropertyMap.fromFile(dbPropertiesFilePath));

        dbService.execute(Arrays.stream(dbInitScriptFilePaths)
                .map(Paths::get)
                .flatMap(ExceptionalFunction.toUncheckedFunction(Files::lines))
                .collect(Collectors.joining()).split(";")
        ).executeOrThrowUnchecked();

        return dbService;
    }

    static DbService from(PropertyMap propertyMap) {
        assert propertyMap.containsKey(DRIVER_KEY);
        assert propertyMap.containsKey(URL_KEY);
        return from(
                propertyMap.remove(DRIVER_KEY),
                propertyMap.remove(URL_KEY),
                propertyMap.toProperties());
    }

    default <T> ExceptionalSupplier<T, SQLException> mapConnection(
            ExceptionalFunction<Connection, T, SQLException> connectionMapper) {
        return () -> {
            try (final Connection connection = get()) {
                return connectionMapper.get(connection);
            }
        };
    }

    default <T> ExceptionalSupplier<T, SQLException> mapStatement(
            ExceptionalFunction<Statement, T, SQLException> statementMapper) {
        return mapConnection(connection -> {
            try (final Statement statement = connection.createStatement()) {
                return statementMapper.get(statement);
            }
        });
    }

    default <T> ExceptionalSupplier<T, SQLException> mapPreparedStatement(
            String sql,
            ExceptionalFunction<PreparedStatement, T, SQLException> preparedStatementMapper) {
        return mapConnection(connection -> {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                return preparedStatementMapper.get(preparedStatement);
            }
        });
    }

    default <T> ExceptionalSupplier<T, SQLException> mapResultSet(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> resultSetMapper) {
        return mapStatement(statement -> {
            try (final ResultSet rs = statement.executeQuery(sql)) {
                return resultSetMapper.get(rs);
            }
        });
    }

    default <T> ExceptionalSupplier<Optional<T>, SQLException> mapRow(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper) {
        return mapResultSet(sql,
                resultSet -> resultSet.next() ? Optional.of(rowMapper.get(resultSet)) : Optional.empty());
    }

    default ExceptionalRunnable<SQLException> mapRows(
            String sql,
            ExceptionalConsumer<ResultSet, SQLException> rowMapper) {
        return mapResultSet(sql, resultSet -> {
            while (resultSet.next())
                rowMapper.call(resultSet);
            return 0;
        })::executeOrThrowUnchecked;
    }

    default <T> ExceptionalRunnable<SQLException> mapAndReduceRows(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
            Consumer<T> reducer) {
        return mapRows(sql, resultSet -> reducer.accept(rowMapper.get(resultSet)));
    }

    default ExceptionalSupplier<Integer, SQLException> execute(String sql) {
        return mapStatement(statement -> statement.executeUpdate(sql));
    }

    default ExceptionalSupplier<int[], SQLException> execute(String... sqlStrings) {
        return mapStatement(statement -> {
            Arrays.stream(sqlStrings)
                    .forEach(ExceptionalConsumer.toUncheckedConsumer(statement::addBatch));
            return statement.executeBatch();
        });
    }

    default ExceptionalSupplier<Integer, SQLException> execute(String sql, Object... values) {
        return mapPreparedStatement(sql, preparedStatement -> {
            for (int i = 0; i < values.length; )
                preparedStatement.setObject(i + 1, values[i++]);
            return preparedStatement.executeUpdate();
        });
    }

    default <T> ExceptionalSupplier<T, SQLException> request(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> resultSetMapper,
            Object... values) {
        return mapPreparedStatement(sql, preparedStatement -> {
            for (int i = 0; i < values.length; )
                preparedStatement.setObject(i + 1, values[i++]);
            return resultSetMapper.get(preparedStatement.executeQuery());
        });
    }
}
