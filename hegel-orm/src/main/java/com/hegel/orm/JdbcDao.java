package com.hegel.orm;

import com.hegel.core.functions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hegel.core.functions.ExceptionalConsumer.toUncheckedConsumer;

@FunctionalInterface
public interface JdbcDao<J extends JdbcDao<J>> extends Supplier<Connection> {

    default <T> ExceptionalSupplier<T, SQLException> connectionMapper(
            ExceptionalFunction<Connection, T, SQLException> connectionMapper) {
        return () -> {
            try (Connection connection = get()) {
                return connectionMapper.get(connection);
            }
        };
    }

    default <T> ExceptionalSupplier<T, SQLException> statementMapper(
            ExceptionalFunction<Statement, T, SQLException> statementMapper) {
        return connectionMapper(connection -> {
            try (Statement statement = connection.createStatement()) {
                return statementMapper.get(statement);
            }
        });
    }

    default J executeScripts(Path... sqlFilePaths) {
        statementMapper(statement -> {
            Arrays.stream(sqlFilePaths)
                    .map(ExceptionalFunction.toUncheckedFunction(Files::readAllBytes))
                    .map(String::new)
                    .map(s -> s.split(";"))
                    .flatMap(Arrays::stream)
                    .forEach(toUncheckedConsumer(statement::addBatch));
            return statement.executeBatch();
        }).executeOrThrowUnchecked();
        //noinspection unchecked
        return (J) this;
    }

    default <T> ExceptionalSupplier<T, SQLException> resultSetMapper(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> resultSetMapper) {
        return statementMapper(statement -> {
            try (final ResultSet rs = statement.executeQuery(sql)) {
                return resultSetMapper.get(rs);
            }
        });
    }

    default ExceptionalSupplier<Integer, SQLException> executor(String sql) {
        return statementMapper(statement -> statement.executeUpdate(sql));
    }

    default <T> ExceptionalSupplier<Optional<T>, SQLException> rowMapper(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper) {
        return resultSetMapper(sql,
                resultSet -> resultSet.next() ? Optional.of(rowMapper.get(resultSet)) : Optional.empty());
    }

    default ExceptionalRunnable<SQLException> rowsMapper(
            String sql,
            ExceptionalConsumer<ResultSet, SQLException> rowMapper) {
        return resultSetMapper(sql, resultSet -> {
            while (resultSet.next())
                rowMapper.call(resultSet);
            return 0;
        })::executeOrThrowUnchecked;
    }

    default <T> ExceptionalRunnable<SQLException> rowsMapReducer(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
            Consumer<T> reducer) {
        return rowsMapper(sql, resultSet -> reducer.accept(rowMapper.get(resultSet)));
    }

    default <T, C extends Collection<T>> Exceptional<C, SQLException> collect(
            String sql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
            Supplier<C> collectionConstructor) {
        C collection = collectionConstructor.get();
        try {
            rowsMapReducer(sql, rowMapper, collection::add)
                    .call();
        } catch (SQLException e) {
            return Exceptional.withException(e);
        }
        return Exceptional.withValue(collection);
    }

    default <T> ExceptionalVarFunction<Object, T, SQLException> preparedStatementMapper(
            String preparedSql,
            ExceptionalFunction<PreparedStatement, T, SQLException> preparedStatementMapper) {
        return params -> connectionMapper(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedSql)) {
                for (int index = 0; index < params.length; index++)
                    preparedStatement.setObject(index + 1, params[index]);
                return preparedStatementMapper.get(preparedStatement);
            }
        }).call();
    }

    default ExceptionalVarFunction<Object, Integer, SQLException> preparedExecutor(String preparedSql) {
        return preparedStatementMapper(preparedSql, PreparedStatement::executeUpdate);
    }

    default <T> ExceptionalVarFunction<Object, T, SQLException> preparedResultSetMapper(
            String preparedSql,
            ExceptionalFunction<ResultSet, T, SQLException> preparedResultSetMapper) {
        return preparedStatementMapper(preparedSql, preparedStatement -> {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return preparedResultSetMapper.get(resultSet);
            }
        });
    }

    default <T> ExceptionalVarFunction<Object, Optional<T>, SQLException> mapPreparedRow(
            String preparedSql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper) {
        return preparedResultSetMapper(preparedSql,
                resultSet -> resultSet.next() ? Optional.of(rowMapper.get(resultSet)) : Optional.empty());
    }

    default ExceptionalVarConsumer<Object, SQLException> mapPreparedRows(
            String preparedSql,
            ExceptionalConsumer<ResultSet, SQLException> rowMapper) {
        return preparedResultSetMapper(preparedSql, resultSet -> {
            while (resultSet.next())
                rowMapper.call(resultSet);
            return 0;
        })::executeOrThrowUnchecked;
    }

    default <T> ExceptionalVarConsumer<Object, SQLException> mapAndReducePreparedRows(
            String preparedSql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
            Consumer<T> reducer) {
        return mapPreparedRows(preparedSql, resultSet -> reducer.accept(rowMapper.get(resultSet)));
    }

    default <T, C extends Collection<T>> ExceptionalVarFunction<Object, C, SQLException> preparedCollect(
            String preparedSql,
            ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
            Supplier<C> collectionConstructor) {
        return params -> {
            C collection = collectionConstructor.get();
            mapAndReducePreparedRows(preparedSql, rowMapper, collection::add).call(params);
            return collection;
        };
    }

    /**
     * @deprecated use {@link #preparedCollect(String, ExceptionalFunction, Supplier)} method.
     */
    @Deprecated
    default <T> Stream<T> getStream(String sql,
                                    ExceptionalFunction<ResultSet, T, SQLException> rowMapper,
                                    Object... params) {
        return preparedCollect(sql, rowMapper, HashSet::new)
                .getOrThrowUnchecked(params)
                .stream();
    }

    default <T> Collection<T> getObjects(Constructor<T> constructor) {
        return collect(
                getQueryString(constructor),
                resultSet -> ExceptionalFunction.getOrThrowUnchecked(constructor::newInstance,
                        Arrays.stream(constructor.getParameters())
                                .map(parameter -> convert(parameter.getType(),
                                        ExceptionalFunction.getOrThrowUnchecked(
                                                resultSet::getObject,
                                                toDbName(parameter.getName()))))
                                .toArray()),
                ArrayList<T>::new)
                .getOrThrowUnchecked();
    }

//    default <T> Collection<T> getObjects(Method method) {
//        return collect(
//                getQueryString(method),
//                resultSet -> {
//                    return ExceptionalFunction.getOrThrowUnchecked(params -> method.invoke(null, params),
//                            Arrays.stream(method.getParameters())
//                                    .map(parameter -> convert(parameter.getType(),
//                                            ExceptionalFunction.getOrThrowUnchecked(
//                                                    resultSet::getObject,
//                                                    toDbName(parameter.getName()))))
//                                    .toArray());
//                },
//                ArrayList<T>::new)
//                .getOrThrowUnchecked();
//    }

    static <T> T convert(Class<T> aClass, Object o) {
        //noinspection unchecked
        return (T) (aClass.isInstance(o) ? o :
                aClass.equals(LocalDate.class) && o instanceof Date ? ((Date) o).toLocalDate() :
                        // add other transformers here
                        o);
    }

    static String getQueryString(Executable executable) {
        String typeName = executable.getAnnotatedReturnType().getType().getTypeName();
        return "SELECT "
                + Arrays.stream(executable.getParameters())
                .map(Parameter::getName)
                .map(JdbcDao::toDbName)
                .collect(Collectors.joining(", "))
                + " FROM "
                + typeName.substring(typeName.lastIndexOf('.') + 1);
    }

    static String toDbName(String name) {
        return name.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    static String toCamelCase(String name) {
        String[] words = name.split("_");
        return words.length == 1 ? name : words[0]
                + Arrays.stream(Arrays.copyOfRange(words, 1, words.length))
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .collect(Collectors.joining());
    }
}
