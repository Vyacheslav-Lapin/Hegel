package com.hegel.orm;

import com.hegel.core.functions.ExceptionalFunction;
import com.hegel.core.Private;
import com.hegel.orm.columns.Column;
import com.hegel.reflect.BaseType;
import com.hegel.reflect.Class;
import com.hegel.reflect.Parameter;
import com.hegel.reflect.fields.Field;
import com.hegel.reflect.Constructor;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DbService {

    @Private
    String jdbcUrl();

    @Private
    Properties jdbcProperties();

    static DbService create(String driver, String jdbcUrl, Properties properties) {

        try {
            java.lang.Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new DbService() {
            @Override
            public String jdbcUrl() {
                return jdbcUrl;
            }

            @Override
            public Properties jdbcProperties() {
                return properties;
            }
        };
    }

    static DbService create(String driver, String jdbcUrl, String user, String password) {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        return create(driver, jdbcUrl, properties);
    }

    default Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl(), jdbcProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default int execute(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default int[] execute(String... sqlStrings) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            Arrays.stream(sqlStrings).forEach(sql -> addBatch(statement, sql));
            return statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Private
    static void addBatch(Statement statement, String sql) {
        try {
            statement.addBatch(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    default int execute(String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = setParams(connection.prepareStatement(sql), params)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default <T, E extends SQLException> Optional<T> request(ExceptionalFunction<ResultSet, T, E> template, String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? template.apply(resultSet) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default <T, E extends SQLException> Optional<Collection<T>> requestCollection(ExceptionalFunction<ResultSet, T, E> template, String sql) {
        return request(rs -> toCollection(rs, template), sql);
    }

    default <T, E extends SQLException> Optional<T> request(ExceptionalFunction<ResultSet, T, E> template, String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = setParams(connection.prepareStatement(sql), params);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next() ? template.apply(resultSet) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    default <T, E extends SQLException> Optional<Collection<T>> requestCollection(ExceptionalFunction<ResultSet, T, E> template, String sql, Object... params) {
        return request(rs -> toCollection(rs, template), sql, params);
    }

    default <T> Stream<T> requestObjects(java.lang.Class<T> aClass) {
        return requestObjects(Class.wrap(aClass));
    }

    default <T> Stream<T> requestObjects(Class<T> aClass) {

        Constructor<T> constructor = aClass.findConstructorByParamNames(
                aClass.dynamicFields()
                        .map(Field::getName)
                        .collect(Collectors.toSet()))
                .orElseThrow(() -> new AssertionError("Can`t find relevant constructor!"));

        return requestCollection(
                rs -> toObject(rs, constructor),
                selectQuery(Table.wrap(aClass)))
                .get().stream();
    }

    static <C> String selectQuery(Table<C> table) {
        return "SELECT " + table.columns().map(Column::toSqlName).collect(Collectors.joining(", "))
                + " FROM " + table.getName();
    }

    @Private
    static <T> T toObject(ResultSet rs, Constructor<T> constructor) {
        return constructor.execute(constructor.parameters().map(parameter -> invokeRsMethod(rs, parameter)).toArray());
    }

    @Private
    static <T, E extends SQLException> Collection<T> toCollection(ResultSet rs, ExceptionalFunction<ResultSet, T, E> template) throws SQLException {
        Collection<T> ts = new ArrayList<>();
        do {
            template.apply(rs).ifPresent(ts::add);
        } while (rs.next());
        return ts;
    }


    @SuppressWarnings("unchecked")
    @Private
    static <T> T invokeRsMethod(ResultSet rs, Parameter<T, ?> parameter) {
        try {
            Class<?> type = parameter.getType();
            Optional<BaseType> paramType = BaseType.from(type);
            switch (paramType.get()) { // TODO: 3/18/2016 Optional.map( <Visitor> ) - ?
                case BOOLEAN:
                    return (T) Boolean.valueOf(rs.getBoolean(parameter.getName()));
                case INT:
                    return (T) Integer.valueOf(rs.getInt(parameter.getName()));
                case DOUBLE:
                    return (T) Double.valueOf(rs.getDouble(parameter.getName()));
                case LONG:
                    return (T) Long.valueOf(rs.getLong(parameter.getName()));
                case FLOAT:
                    return (T) Float.valueOf(rs.getFloat(parameter.getName()));
                case CHAR:
                    return (T) Character.valueOf(rs.getString(parameter.getName()).charAt(0));
                case SHORT:
                    return (T) Short.valueOf(rs.getShort(parameter.getName()));
                case BYTE:
                    return (T) Byte.valueOf(rs.getByte(parameter.getName()));
                case REFERENCE:
                    if (type.toSrc() == String.class)
                        return (T) rs.getString(parameter.getName());
                default:
                    return null; // TODO: 3/18/2016
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Private
    static PreparedStatement setParams(PreparedStatement preparedStatement, Object... params) {
        Object param;
        for (int i = 0; i < params.length; )
            try {
                if ((param = params[i++]) instanceof Number)
                    if (param instanceof Integer) preparedStatement.setInt(i, (Integer) param);
                    else if (param instanceof Double) preparedStatement.setDouble(i, (Double) param);
                    else if (param instanceof Long) preparedStatement.setLong(i, (Long) param);
                    else if (param instanceof Float) preparedStatement.setFloat(i, (Float) param);
                    else if (param instanceof Byte) preparedStatement.setByte(i, (Byte) param);
                    else preparedStatement.setShort(i, (Short) param);
                else if (param instanceof String || param instanceof Character)
                    preparedStatement.setString(i, param.toString());
                else if (param instanceof java.util.Date)
                    preparedStatement.setDate(i, new java.sql.Date(((java.util.Date) param).getTime()));
                else if (param instanceof Boolean) preparedStatement.setBoolean(i, (Boolean) param);
                else preparedStatement.setObject(i, param);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return preparedStatement;
    }
}
