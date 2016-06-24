package com.hegel.orm;

public interface ConnectionPool extends DbService {

    static ConnectionPool get() {
        return null;
    }
}
