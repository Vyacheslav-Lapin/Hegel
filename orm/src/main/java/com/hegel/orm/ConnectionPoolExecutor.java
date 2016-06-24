package com.hegel.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface ConnectionPoolExecutor extends ConnectionPool {

    static ConnectionPool get() {
        return null; // TODO: 3/19/2016
    }

    default <T> CompletableFuture<T> select(Function<Connection, T> connectionExtractor) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                return connectionExtractor.apply(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    default <T> CompletableFuture<Collection<T>> select(Function<Connection, T> extractor) {
//
//    }
}
