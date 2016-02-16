package com.hegel.orm;

import java.sql.Connection;

public interface DbService {

    Connection getConnection();
}
