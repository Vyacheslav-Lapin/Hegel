package com.hegel.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DbService implements DbService {

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:~/hegeltest");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
